package at.roteskreuz.covidapp.service;

import at.roteskreuz.covidapp.domain.ExportBatch;
import at.roteskreuz.covidapp.domain.Exposure;
import at.roteskreuz.covidapp.domain.SignatureInfo;
import at.roteskreuz.covidapp.properties.ExportProperties;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
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
public class BlobstoreService {
	
	private static final String FILENAME_SUFFIX = ".zip";
	
	private final ExportService exportService;
	private final ExportProperties exportProperties;
	
	
	@Value("${azure.storage.connection-string:}")
	private String storageConnectionString;	
	
	
	public String createFile(ExportBatch batch, List<Exposure> exposures, int batchNum, int batchSize, List<SignatureInfo> exportSigners) throws Exception {
		String objectName = exportPathAndFilename(batch, batchNum);
		switch (exportProperties.getBlobstoreType()) {
			case AZURE_CLOUD_STORAGE: {
				objectName =exportFilename(batch, batchNum);
				byte[] data = exportService.marshalExportFile(batch, exposures, batchNum, batchSize, exportSigners);
				CloudStorageAccount cloudStorageAccount = CloudStorageAccount.parse(storageConnectionString);
				CloudBlobContainer container =cloudStorageAccount.createCloudBlobClient().getContainerReference(batch.getFilenameRoot());
				CloudBlockBlob blockBlobReference = container.getBlockBlobReference(objectName);
				blockBlobReference.upload(new ByteArrayInputStream(data) , data.length);
				break;
			}
			case FILESYSTEM: {
				byte[] data = exportService.marshalExportFile(batch, exposures, batchNum, batchSize, exportSigners);
				File file = new File(objectName);
				file.getParentFile().mkdirs();
				Files.write(Paths.get(objectName), data);
				break;
			}
			case NONE: {
				break;
			}
			default: {
			}
		}
		return objectName;
	}

	private String exportFilename(ExportBatch batch, int batchNum) {
		return String.format("%d-%05d%s", batch.getStartTimestamp().toEpochSecond(ZoneOffset.UTC), batchNum, FILENAME_SUFFIX);
	}

	private String exportPathAndFilename(ExportBatch batch, int batchNum) {
		return String.format("%s/%s", batch.getFilenameRoot(), exportFilename(batch, batchNum));
	}
	
}
