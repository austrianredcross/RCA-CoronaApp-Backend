package at.roteskreuz.covidapp.service;

import at.roteskreuz.covidapp.blobstore.Blobstore;
import at.roteskreuz.covidapp.config.ApplicationConfig;
import at.roteskreuz.covidapp.domain.ExportConfig;
import at.roteskreuz.covidapp.domain.ExportFile;
import at.roteskreuz.covidapp.domain.Exposure;
import at.roteskreuz.covidapp.domain.SignatureInfo;
import at.roteskreuz.covidapp.exception.LockNotAcquiredException;
import at.roteskreuz.covidapp.model.ApiResponse;
import at.roteskreuz.covidapp.model.IndexFile;
import at.roteskreuz.covidapp.model.IndexFileBatch;
import at.roteskreuz.covidapp.properties.ExportProperties;
import at.roteskreuz.covidapp.protobuf.Export;
import at.roteskreuz.covidapp.protobuf.Export.TemporaryExposureKey;
import at.roteskreuz.covidapp.protobuf.Export.TemporaryExposureKeyExport;
import at.roteskreuz.covidapp.repository.ExportConfigRepository;
import at.roteskreuz.covidapp.repository.ExportFileRepository;
import at.roteskreuz.covidapp.sign.Signer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;
import io.micrometer.core.instrument.util.StringUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service class that exports exposures
 *
 * @author Zoltán Puskai
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExportService {

	private static final String FILENAME_SUFFIX = ".zip";
	private static final String EXPORT_BINARY_NAME = "export.bin";
	private static final String EXPORT_SIGNATURE_NAME = "export.sig";
	private static final String ALGORITHM = "1.2.840.10045.4.3.2";

	private final ExportProperties exportProperties;
	private final ExposureService exposureService;
	private final LockService lockService;
	private final Blobstore blobstore;
	private final ObjectMapper objectMapper;
	private final ExportConfigRepository exportConfigRepository;
	private final ExportFileRepository exportFileRepository;
	private final Signer signer;

	/**
	 * Exports files for every valid export configuration
	 *
	 * @return
	 * @throws Exception
	 */
	public ApiResponse export() throws Exception {
		String lockId = "export_files";
		try {
			LocalDateTime releaseTimestamp = lockService.acquireLock(lockId, exportProperties.getCreateTimeout());
			LocalDateTime now = LocalDateTime.now();

			List<ExportConfig> exportConfigs = exportConfigRepository.findAllByDate(now);
			for (ExportConfig exportConfig : exportConfigs) {
				exportConfig(exportConfig);
			}
			log.info(String.format("Processed %s configs.", exportConfigs.size()));
			boolean unlocked = lockService.releaseLock(lockId, releaseTimestamp);
			log.debug(String.format("Removed lock for id: %s with result: %b", lockId, unlocked));
		} catch (LockNotAcquiredException e) {
			log.error("Could not acquire lock for exporting files");
			//fail silently
		}
		return ApiResponse.ok();
	}

	/**
	 * Converts the inputs into an encoded byte array.
	 *
	 * @param region exported region
	 * @param startTimestamp start timestamp of the export
	 * @param endTimestamp end timestamp of the export
	 * @param exposures exposures to be exported
	 * @param batchNum batch number
	 * @param batchSize batch size
	 * @param exportSigners signers to sign exported data
	 * @return
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	//MarshalExportFile converts the inputs into an encoded byte array.	
	private byte[] marshalExportFile(String region, LocalDateTime startTimestamp, LocalDateTime endTimestamp, List<Exposure> exposures, int batchNum, int batchSize, List<SignatureInfo> exportSigners) throws IOException, GeneralSecurityException {
		// create main exposure key export binary
		byte[] expContents = marshalContents(region, startTimestamp, endTimestamp, exposures, batchNum, batchSize, exportSigners);
		// create signature file
		byte[] sigContents = marshalSignature(expContents, batchNum, batchSize, exportSigners);

		// create compressed archive of binary and signature
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (ZipOutputStream zos = new ZipOutputStream(baos)) {
			ZipEntry binEntry = new ZipEntry(EXPORT_BINARY_NAME);
			binEntry.setSize(expContents.length);
			zos.putNextEntry(binEntry);
			zos.write(expContents);
			zos.closeEntry();

			ZipEntry sigEntry = new ZipEntry(EXPORT_SIGNATURE_NAME);
			sigEntry.setSize(sigContents.length);
			zos.putNextEntry(sigEntry);
			zos.write(sigContents);
			zos.closeEntry();
		}
		return baos.toByteArray();
	}

	private byte[] marshalContents(String region, LocalDateTime startTimestamp, LocalDateTime endTimestamp, List<Exposure> exposures, int batchNum, int batchSize, List<SignatureInfo> exportSigners) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		output.write("EK Export v1    ".getBytes());

		exposures.sort(Comparator.comparing(c -> c.getExposureKey()));
		List<TemporaryExposureKey> temporaryExposureKeys = new ArrayList<>();

		exposures.forEach(exp -> {
			TemporaryExposureKey.Builder exposureKeyBuilder = TemporaryExposureKey.newBuilder()
					.setKeyData(ByteString.copyFrom(exp.getExposureKey().getBytes()))
					.setTransmissionRiskLevel(exp.getTransmissionRisk());
			if (exp.getIntervalNumber() != null) {
				exposureKeyBuilder.setRollingStartIntervalNumber(exp.getIntervalNumber());
			}
			if (exp.getIntervalCount() != null) {
				exposureKeyBuilder.setRollingPeriod(exp.getIntervalCount());
			}
			temporaryExposureKeys.add(exposureKeyBuilder.build());
		});

		List<Export.SignatureInfo> signatures = new ArrayList<>();
		exportSigners.forEach(si -> {
			Export.SignatureInfo.Builder signatureBuilder = Export.SignatureInfo.newBuilder()
					.setSignatureAlgorithm(ALGORITHM);
			if (StringUtils.isNotEmpty(si.getAppPackageName())) {
				signatureBuilder.setAndroidPackage(si.getAppPackageName());
			}
			if (StringUtils.isNotEmpty(si.getBundleID())) {
				signatureBuilder.setAppBundleId(si.getBundleID());
			}
			if (StringUtils.isNotEmpty(si.getSigningKeyVersion())) {
				signatureBuilder.setVerificationKeyVersion(si.getSigningKeyVersion());
			}
			if (StringUtils.isNotEmpty(si.getSigningKeyID())) {
				signatureBuilder.setVerificationKeyId(si.getSigningKeyID());
			}
			signatures.add(signatureBuilder.build());
		});

		TemporaryExposureKeyExport.Builder exposureKeyExportBuilder = TemporaryExposureKeyExport.newBuilder()
				.setStartTimestamp(startTimestamp.toEpochSecond(ZoneOffset.UTC))
				.setEndTimestamp(endTimestamp.toEpochSecond(ZoneOffset.UTC))
				.setRegion(region)
				.setBatchNum(batchNum)
				.setBatchSize(batchSize)
				.addAllKeys(temporaryExposureKeys)
				.addAllSignatureInfos(signatures);

		TemporaryExposureKeyExport exposureKeyExport = exposureKeyExportBuilder.build();
		exposureKeyExport.writeTo(output);
		return output.toByteArray();
	}

	private byte[] marshalSignature(byte[] exportContents, int batchNum, int batchSize, List<SignatureInfo> exportSigners) throws IOException, GeneralSecurityException {
		List<Export.TEKSignature> signatures = new ArrayList<>();
		byte[] signature = signer.sign(exportContents);

		for (SignatureInfo si : exportSigners) {
			Export.SignatureInfo.Builder signatureInfoBuilder = Export.SignatureInfo.newBuilder()
					.setSignatureAlgorithm(ALGORITHM);
			if (StringUtils.isNotEmpty(si.getAppPackageName())) {
				signatureInfoBuilder.setAndroidPackage(si.getAppPackageName());
			}
			if (StringUtils.isNotEmpty(si.getBundleID())) {
				signatureInfoBuilder.setAppBundleId(si.getBundleID());
			}
			if (StringUtils.isNotEmpty(si.getSigningKeyVersion())) {
				signatureInfoBuilder.setVerificationKeyVersion(si.getSigningKeyVersion());
			}
			if (StringUtils.isNotEmpty(si.getSigningKeyID())) {
				signatureInfoBuilder.setVerificationKeyId(si.getSigningKeyID());
			}

			Export.TEKSignature teks = Export.TEKSignature.newBuilder()
					.setSignatureInfo(signatureInfoBuilder.build())
					.setBatchNum(batchNum)
					.setBatchSize(batchSize)
					.setSignature(ByteString.copyFrom(signature))
					.build();
			signatures.add(teks);
		}
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		Export.TEKSignatureList signatureList = Export.TEKSignatureList.newBuilder().addAllSignatures(signatures).build();
		signatureList.writeTo(output);
		return output.toByteArray();
	}

	private void exportConfig(ExportConfig config) throws Exception {
		//create the new export files
		LocalDateTime fileDate = LocalDateTime.now();
		
		IndexFile indexFile = new IndexFile();
		LocalDateTime startDate = LocalDate.now().atStartOfDay();
		LocalDateTime fromYellow = startDate.minus(config.getPeriodYellowWarnings());
		LocalDateTime fromRed = startDate.minus(config.getPeriodRedWarnings());
		LocalDateTime until = startDate;
		if (exportProperties.isExportCurrentDay()) {
			until = LocalDateTime.now();
		}
		List<Exposure> redExposures = exposureService.findExposuresForExport(fromRed, until, "red-warning", config.getRegion());
		List<Exposure> yellowExposures = exposureService.findExposuresForExport(fromYellow, until, "yellow-warning", config.getRegion());
		List<Exposure> allExposures = new ArrayList<>();
		allExposures.addAll(redExposures);
		allExposures.addAll(yellowExposures);
		long intervalNumber = startDate.toInstant(ZoneOffset.UTC).getEpochSecond() / ApplicationConfig.INTERVAL_LENGTH.getSeconds();
		log.info("Creating full export file with start date: " + startDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd.")));
		List<String> fullExportFilenames = exportExposures("batch_full", fileDate, config, startDate, until, intervalNumber, allExposures);
		//Writing files to database
		fullExportFilenames.forEach((fileName) -> {
			exportFileRepository.save(new ExportFile(fileName, config.getBucketName(), config, config.getRegion(), fileDate.toEpochSecond(ZoneOffset.UTC)));
		});
		indexFile.setFullBatch(new IndexFileBatch(intervalNumber, fullExportFilenames));
		indexFile.setDailyBatches(new ArrayList<>());
		LocalDateTime date = fromRed;
		while (date.isBefore(until)) {
			long startIntervalNumber = date.toInstant(ZoneOffset.UTC).getEpochSecond() / ApplicationConfig.INTERVAL_LENGTH.getSeconds();
			long endIntervalNumber = date.plusDays(1).toInstant(ZoneOffset.UTC).getEpochSecond() / ApplicationConfig.INTERVAL_LENGTH.getSeconds();
			List<Exposure> exposuresForDate = allExposures.stream().filter(s -> s.getIntervalNumber() >= startIntervalNumber && s.getIntervalNumber() < endIntervalNumber).collect(Collectors.toList());
			log.info("Creating daily export file with start date: " + date.format(DateTimeFormatter.ofPattern("yyyy.MM.dd.")));
			log.trace("Creating file for intervals: " + startIntervalNumber + " - " + endIntervalNumber);
			List<String> dailyExportFilenames = exportExposures("batch",fileDate, config, startDate, date.plusDays(1), startIntervalNumber, exposuresForDate);
			dailyExportFilenames.forEach((fileName) -> {
				exportFileRepository.save(new ExportFile(fileName, config.getBucketName(), config, config.getRegion(), fileDate.toEpochSecond(ZoneOffset.UTC)));
			});			
			indexFile.getDailyBatches().add(new IndexFileBatch(startIntervalNumber, dailyExportFilenames));
			date = date.plusDays(1);
		}
		//createIndexFile
		String indexFileContent =objectMapper.writeValueAsString(indexFile);
		String indexFileName = createIndexFile(fileDate, config, indexFileContent.getBytes());
		
		//Copy over the index file;
		String commonIndexFileName = commonIndexFilename(config);
		blobstore.copy(config.getBucketName(), indexFileName, commonIndexFileName);		
		log.info(String.format("Config %s completed", config.getId()));
		
		
		
		
	}

	private List<String> exportExposures(String filePrefix, LocalDateTime fileDate,  ExportConfig config, LocalDateTime startTimestamp, LocalDateTime endTimestamp, long intervalNumber, List<Exposure> exposuresToExport) throws NoSuchAlgorithmException, Exception {

		List<Exposure> exposures = new ArrayList<>();
		List<List<Exposure>> groups = new ArrayList<>();

		for (Exposure exposure : exposuresToExport) {
			exposures.add(exposure);
			if (exposures.size() == exportProperties.getMaxRecords()) {
				groups.add(exposures);
				exposures = new ArrayList<>();
			}
		}
		// Create a group for any remaining keys.
		if (!exposures.isEmpty()) {
			groups.add(exposures);
		}
		if (groups.isEmpty()) {
			log.info(String.format("No records for export config %s", config.getId()));
		}
		ensureMinNumExposures(exposures, config.getRegion(), exportProperties.getMinRecords(), exportProperties.getPaddingRange());
		// Load the non-expired signature infos associated with this export batch. - in our case we already have them, just have to filter them	
		List<SignatureInfo> sigInfos = config.getSignatureInfos().stream().filter(si -> si.getEndTimestamp() == null || !si.getEndTimestamp().isBefore(LocalDateTime.now())).collect(Collectors.toList());

		// Create the export files.
		int batchSize = groups.size();
		List<String> objectNames = new ArrayList<>();
		for (int i = 0; i < groups.size(); i++) {
			List<Exposure> group = groups.get(i);
			String objectName = createFile(filePrefix, fileDate, config, startTimestamp, endTimestamp, intervalNumber, group, i + 1, batchSize, sigInfos);
			log.info(String.format("Wrote export file %s for config %s", objectName, config.getId()));
			objectNames.add(objectName);
		}
		return objectNames;
	}

	private List<Exposure> ensureMinNumExposures(List<Exposure> exposures, String region, Integer minLength, Integer jitter) throws NoSuchAlgorithmException {

		List<String> diagnosisTypes = Arrays.asList("red-warning", "yellow-warning");

		if (exposures.isEmpty()) {
			return Collections.EMPTY_LIST;
		}

		Random random = new Random();
		int extra = random.nextInt(jitter);
		int target = minLength + extra;
		Integer fromIdx;

		SecureRandom secureRandom = SecureRandom.getInstanceStrong();
		while (exposures.size() < target) {
			// Pieces needed are
			// (1) exposure key, (2) interval number, (3) transmission risk
			// Exposure key is 16 random bytes.
			byte[] bytes = new byte[ApplicationConfig.KEY_LENGTH];
			secureRandom.nextBytes(bytes);

			fromIdx = random.nextInt(exposures.size());
			Integer intervalNumber = exposures.get(fromIdx).getIntervalNumber();

			fromIdx = random.nextInt(exposures.size());
			Integer intervalCount = exposures.get(fromIdx).getIntervalCount();
			String diagnosisType = diagnosisTypes.get(random.nextInt(diagnosisTypes.size()));

			Exposure exposure = new Exposure(new String(bytes), null, region, intervalNumber, intervalCount, diagnosisType);
			// The rest of the publishmodel.Exposure fields are not used in the export file.
			exposures.add(exposure);
		}
		return exposures;
	}

	private String createFile(String filePrefix, LocalDateTime fileDate, ExportConfig config, LocalDateTime startTimestamp, LocalDateTime endTimestamp, Long intervalNumber, List<Exposure> exposures, int batchNum, int batchSize, List<SignatureInfo> exportSigners) throws Exception {
		String objectName = exportFilename(filePrefix, config, fileDate, intervalNumber, batchNum);
		byte[] data = marshalExportFile(config.getRegion(), startTimestamp, endTimestamp, exposures, batchNum, batchSize, exportSigners);
		blobstore.createObject(config.getBucketName(), objectName, data);
		return objectName;
	}
	
	private String exportFilename(String filePrefix, ExportConfig config, LocalDateTime fileDate, Long intervalNumber, int batchNum) {
		return String.format("%s/%d/%s-%d-%d%s", config.getFilenameRoot(), fileDate.toEpochSecond(ZoneOffset.UTC), filePrefix, intervalNumber, batchNum, FILENAME_SUFFIX);
	}

	private String createIndexFile(LocalDateTime fileDate, ExportConfig config, byte[] data) throws Exception {
		String objectName = indexFilename(config, fileDate);
		blobstore.createObject(config.getBucketName(), objectName, data);
		return objectName;
	}	
	
	private String indexFilename(ExportConfig config, LocalDateTime fileDate ) {
		return String.format("%s/%d/%s", config.getFilenameRoot(), fileDate.toEpochSecond(ZoneOffset.UTC), "index.json");
	}

	private String commonIndexFilename(ExportConfig config) {
		return String.format("%s/%s", config.getFilenameRoot(), "index.json");
	}
	
	
}
