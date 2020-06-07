package at.roteskreuz.covidapp.service;

import at.roteskreuz.covidapp.blobstore.Blobstore;
import at.roteskreuz.covidapp.config.ApplicationConfig;
import at.roteskreuz.covidapp.domain.ExportConfig;
import at.roteskreuz.covidapp.domain.Exposure;
import at.roteskreuz.covidapp.exception.LockNotAcquiredException;
import at.roteskreuz.covidapp.model.ApiResponse;
import at.roteskreuz.covidapp.properties.ExportProperties;
import at.roteskreuz.covidapp.repository.ExportConfigRepository;
import at.roteskreuz.covidapp.repository.ExportFileRepository;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Cleans up old files and old exposures
 * Exported files are signed with private keys during the export process.
 * 
 * @author Bernhard Roessler
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CleanupService {

	private static final Duration MIN_CLEANUP_EXPOSURE_TTL = Duration.ofDays(10);	
	private static final Duration MIN_CLEANUP_FILES_TTL = Duration.ofDays(1);	

	private final ExposureService exposureService;
	private final ExportFileRepository exportFileRepository;
	private final Blobstore blobstore;
	private final LockService lockService;
	private final ExportProperties exportProperties;
	private final ExportConfigRepository exportConfigRepository;

	/**
	 * Cleans up old files older
	 * @return API response
	 * @throws java.lang.Exception
	 */
	public ApiResponse cleanup() throws Exception {
		String lockId = "cleanup";
		try {
			LocalDateTime releaseTimestamp = lockService.acquireLock(lockId, exportProperties.getCreateTimeout());
			LocalDateTime now = LocalDateTime.now();

			List<ExportConfig> exportConfigs = exportConfigRepository.findAllByDate(now);
			for (ExportConfig exportConfig : exportConfigs) {
				cleanupConfig(exportConfig);
			}
			log.info(String.format("Processed %s configs.", exportConfigs.size()));
			boolean unlocked = lockService.releaseLock(lockId, releaseTimestamp);
			log.debug(String.format("Removed lock for id: %s with result: %b", lockId, unlocked));
		} catch (LockNotAcquiredException e) {
			log.error("Could not acquire lock for cleaning up");
			//fail silently
		}	
		return ApiResponse.ok();
	}
	
	
	private void cleanupConfig(ExportConfig config) throws Exception {
		cleanupFiles(config);
		cleanupExposures(config);
	}
	
	private void cleanupFiles(ExportConfig config) throws Exception {
		LocalDateTime deletionDate = getCutOffDate(config.getPeriodOfKeepingFiles(), MIN_CLEANUP_FILES_TTL);
		//select directories that are older than deletionDate
		Set<String> directories = exportFileRepository.findByConfigAndTimestampLessThan(config, deletionDate.toEpochSecond(ZoneOffset.UTC)).stream().map(s-> ""+ s.getTimestamp()).collect(Collectors.toSet());
		for (String directory : directories) {
			blobstore.deleteObject(config.getBucketName(), config.getFilenameRoot() + "/" +  directory);
		}
	}

	private void cleanupExposures(ExportConfig config) {
		LocalDateTime deletionDate = getCutOffDate(config.getExposureCleanupPeriod(), MIN_CLEANUP_EXPOSURE_TTL);
		long intervalNumber = deletionDate.toInstant(ZoneOffset.UTC).getEpochSecond() / ApplicationConfig.INTERVAL_LENGTH.getSeconds();
		List<Exposure> delExposures = exposureService.cleanUpExposures((int)intervalNumber, config.getRegion());
		log.info(String.format("%d Exposures deleted for config %d", delExposures == null ? 0 : delExposures.size(), config.getId()));
	}	
	

	private LocalDateTime getCutOffDate(Duration cleanupTtl, Duration minimumDuration) {
			if (cleanupTtl.compareTo(minimumDuration) < 0) {
			cleanupTtl = minimumDuration;
		}
		return LocalDate.now().atStartOfDay().minus(cleanupTtl);
	}


}
