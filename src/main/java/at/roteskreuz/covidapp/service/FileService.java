package at.roteskreuz.covidapp.service;

import at.roteskreuz.covidapp.domain.ExportBatch;
import at.roteskreuz.covidapp.domain.Exposure;
import at.roteskreuz.covidapp.domain.SignatureInfo;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author zolika
 */
@Service
@RequiredArgsConstructor
public class FileService {

	private static final String FILENAME_SUFFIX = ".zip";

	private final ExportService exportService;

	public void createFile(ExportBatch batch, List<Exposure> exposures, int batchNum, int batchSize, List<SignatureInfo> exportSigners) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, InvalidKeyException, SignatureException, IOException, InvalidKeySpecException {
		byte[] data = exportService.marshalExportFile(batch, exposures, batchNum, batchSize, exportSigners);
		String objectName = exportFilename(batch, batchNum);
		File file = new File(objectName);
		file.getParentFile().mkdirs();
		Files.write(Paths.get(objectName), data);
	}

	private String exportFilename(ExportBatch batch, int batchNum) {
		return String.format("%s/%d-%05d%s", batch.getFilenameRoot(), batch.getStartTimestamp().toEpochSecond(ZoneOffset.UTC), batchNum, FILENAME_SUFFIX);
	}

}
