package at.roteskreuz.covidapp.blobstore;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import java.io.ByteArrayInputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author zolika
 */
@Slf4j
public class AzureBlobstore implements Blobstore {

	@Value("${azure.storage.connection-string:}")
	private String storageConnectionString;	
	
	@Override
	public void createObject(String bucket, String objectName, byte[] contents) throws Exception {
		log.debug(String.format("Azure blobstore will create file for bucket: %s and objectName: %s",bucket, objectName));
		CloudStorageAccount cloudStorageAccount = CloudStorageAccount.parse(storageConnectionString);
		CloudBlobContainer container = cloudStorageAccount.createCloudBlobClient().getContainerReference(bucket);
		CloudBlockBlob blockBlobReference = container.getBlockBlobReference(objectName);
		blockBlobReference.upload(new ByteArrayInputStream(contents) , contents.length);
	}

	@Override
	public boolean deleteObject(String bucket, String objectName) throws Exception  {
		log.debug(String.format("Azure blobstore will delete file for bucket: %s and objectName: %s",bucket, objectName));
		CloudStorageAccount cloudStorageAccount = CloudStorageAccount.parse(storageConnectionString);
		CloudBlobContainer container = cloudStorageAccount.createCloudBlobClient().getContainerReference(bucket);
		CloudBlockBlob blockBlobReference = container.getBlockBlobReference(objectName);
		return blockBlobReference.deleteIfExists();
	}
}
