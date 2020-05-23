package at.roteskreuz.covidapp.service;

import at.roteskreuz.covidapp.domain.ExportBatch;
import at.roteskreuz.covidapp.domain.Exposure;
import at.roteskreuz.covidapp.domain.SignatureInfo;
import at.roteskreuz.covidapp.protobuf.Export;
import at.roteskreuz.covidapp.protobuf.Export.TemporaryExposureKey;
import at.roteskreuz.covidapp.protobuf.Export.TemporaryExposureKeyExport;
import com.google.protobuf.ByteString;
import io.micrometer.core.instrument.util.StringUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

/**
 *
 * @author zolika
 */
@Service
@RequiredArgsConstructor
public class ExportService {

	private static final String EXPORT_BINARY_NAME = "export.bin";
	private static final String EXPORT_SIGNATURE_NAME = "export.sig";
	private static final String ALGORITHM = "1.2.840.10045.4.3.2";

	private final Sha256Service sha256Service;

	//MarshalExportFile converts the inputs into an encoded byte array.
	public byte[] marshalExportFile(ExportBatch batch, List<Exposure> exposures, int batchNum, int batchSize, List<SignatureInfo> exportSigners) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, InvalidKeyException, SignatureException, IOException, InvalidKeySpecException {
		// create main exposure key export binary
		byte[] expContents = marshalContents(batch, exposures, batchNum, batchSize, exportSigners);
		// create signature file
		byte[] sigContents = marshalSignature(batch, expContents, batchNum, batchSize, exportSigners);

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

	private byte[] marshalContents(ExportBatch batch, List<Exposure> exposures, int batchNum, int batchSize, List<SignatureInfo> exportSigners) throws IOException {
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
				.setStartTimestamp(batch.getStartTimestamp().toEpochSecond(ZoneOffset.UTC))
				.setEndTimestamp(batch.getEndTimestamp().toEpochSecond(ZoneOffset.UTC))
				.setRegion(batch.getRegion())
				.setBatchNum(batchNum)
				.setBatchSize(batchSize)
				.addAllKeys(temporaryExposureKeys)
				.addAllSignatureInfos(signatures);

		TemporaryExposureKeyExport exposureKeyExport = exposureKeyExportBuilder.build();
		exposureKeyExport.writeTo(output);
		return output.toByteArray();
	}

	private byte[] marshalSignature(ExportBatch batch, byte[] exportContents, int batchNum, int batchSize, List<SignatureInfo> exportSigners) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, InvalidKeyException, SignatureException, IOException, InvalidKeySpecException {
		List<Export.TEKSignature> signatures = new ArrayList<>();
		byte[] signiture = generateSignature(exportContents);

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
					.setSignature(ByteString.copyFrom(signiture))
					.build();
			signatures.add(teks);
		}
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		Export.TEKSignatureList signatureList = Export.TEKSignatureList.newBuilder().addAllSignatures(signatures).build();
		signatureList.writeTo(output);
		return output.toByteArray();
	}

	private byte[] generateSignature(byte[] data) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, InvalidKeyException, SignatureException, IOException, InvalidKeySpecException {
		Signature ecdsa = Signature.getInstance("SHA256withECDSA");
		File resource = new ClassPathResource("private.der").getFile();
		ecdsa.initSign(getPrivateKey(resource));
		ecdsa.update(data);
		//System.out.println("Verify: " + ecdsa.verify(ecdsa.sign()));
		return ecdsa.sign();
	}

	private PrivateKey getPrivateKey(File file) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] keyBytes = Files.readAllBytes(file.toPath());
		PKCS8EncodedKeySpec spec
				= new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("EC");
		return kf.generatePrivate(spec);
	}

}
