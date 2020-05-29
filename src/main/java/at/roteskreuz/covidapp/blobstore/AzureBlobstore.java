package at.roteskreuz.covidapp.blobstore;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import java.io.ByteArrayInputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/**
 * Blobstore to store files in the Azure Blobstore
 *
 * @author Zoltán Puskai
 */
@Slf4j
public class AzureBlobstore implements Blobstore {

	@Value("${azure.storage.connection-string:}")
	private String storageConnectionString;	
	
	/**
	 * Creates a file in the blobstore
	 * @param container name of the container 
	 * @param objectName name of the object to be stored
	 * @param contents data to be stored
	 * @throws Exception 
	 */
	@Override
	public void createObject(String container, String objectName, byte[] contents) throws Exception {
		log.debug(String.format("Azure blobstore will create file for bucket: %s and objectName: %s",container, objectName));
		CloudStorageAccount cloudStorageAccount = CloudStorageAccount.parse(storageConnectionString);
		CloudBlobContainer cloudContainer = cloudStorageAccount.createCloudBlobClient().getContainerReference(container);
		CloudBlockBlob blockBlobReference = cloudContainer.getBlockBlobReference(objectName);
		blockBlobReference.upload(new ByteArrayInputStream(contents) , contents.length);
	}

	/**
	 * Deletes a file from the blobstore
	 * @param container name of the container 
	 * @param objectName name of the object to be deleted
	 * @return
	 * @throws Exception 
	 */
	@Override
	public boolean deleteObject(String container, String objectName) throws Exception  {
		log.debug(String.format("Azure blobstore will delete file for bucket: %s and objectName: %s",container, objectName));
		CloudStorageAccount cloudStorageAccount = CloudStorageAccount.parse(storageConnectionString);
		CloudBlobContainer cloudContainer = cloudStorageAccount.createCloudBlobClient().getContainerReference(container);
		CloudBlockBlob blockBlobReference = cloudContainer.getBlockBlobReference(objectName);
		return blockBlobReference.deleteIfExists();
	}
}
