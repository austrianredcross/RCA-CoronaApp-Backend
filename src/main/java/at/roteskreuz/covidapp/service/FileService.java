package at.roteskreuz.covidapp.service;

import at.roteskreuz.covidapp.domain.ExportBatch;
import at.roteskreuz.covidapp.domain.Exposure;
import at.roteskreuz.covidapp.domain.SignatureInfo;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
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
import org.springframework.beans.factory.annotation.Value;
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
	
	@Value("${application.storage.type:filesystem}")
	private String storageType;	
	
	@Value("${azure.storage.connection-string:}")
	private String storageConnectionString;	
	
	
	public void createFile(ExportBatch batch, List<Exposure> exposures, int batchNum, int batchSize, List<SignatureInfo> exportSigners) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, InvalidKeyException, SignatureException, IOException, InvalidKeySpecException, URISyntaxException, StorageException {
		byte[] data = exportService.marshalExportFile(batch, exposures, batchNum, batchSize, exportSigners);
		switch (storageType) {
			case "azure": {
				CloudStorageAccount cloudStorageAccount = CloudStorageAccount.parse(storageConnectionString);
				CloudBlobContainer container =cloudStorageAccount.createCloudBlobClient().getContainerReference(batch.getFilenameRoot());
				CloudBlockBlob blockBlobReference = container.getBlockBlobReference(exportFilename(batch, batchNum));
				blockBlobReference.upload(new ByteArrayInputStream(data) , data.length);
				break;
			}
			case "filesystem": {
				String objectName = exportPathAndFilename(batch, batchNum);
				File file = new File(objectName);
				file.getParentFile().mkdirs();
				Files.write(Paths.get(objectName), data);
				break;
			}
		}
	}

	private String exportFilename(ExportBatch batch, int batchNum) {
		return String.format("%d-%05d%s", batch.getStartTimestamp().toEpochSecond(ZoneOffset.UTC), batchNum, FILENAME_SUFFIX);
	}

	private String exportPathAndFilename(ExportBatch batch, int batchNum) {
		return String.format("%s/%s", batch.getFilenameRoot(), exportFilename(batch, batchNum));
	}
	
}
