package at.roteskreuz.covidapp.blobstore;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlobDirectory;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;
import java.io.ByteArrayInputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/**
 * Blobstore implements the Blob interface and provides the ability
 * write files to Azure Blobstore
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
		log.debug(String.format("Azure blobstore will create file for container: %s and objectName: %s",container, objectName));
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
		log.debug(String.format("Azure blobstore will delete file for container: %s and objectName: %s",container, objectName));
		CloudStorageAccount cloudStorageAccount = CloudStorageAccount.parse(storageConnectionString);
		CloudBlobContainer cloudContainer = cloudStorageAccount.createCloudBlobClient().getContainerReference(container);
		try {
			//try to delete it as a directory
			CloudBlobDirectory directory = cloudContainer.getDirectoryReference("objectName");
			for (ListBlobItem item : directory.listBlobs()) {
				if (item instanceof CloudBlob) {
					((CloudBlob)item).deleteIfExists();
				}
			}
			return true;
		} catch (Exception e) {
			//try to delete it as a file
			CloudBlockBlob blockBlobReference = cloudContainer.getBlockBlobReference(objectName);
			return blockBlobReference.deleteIfExists();
		}
	}
	/**
	 * Copies a file (and replaces if destination exists)
	 * @param container
	 * @param sourcePath
	 * @param destinationPath
	 * @throws Exception 
	 */
	@Override
	public void copy(String container, String sourcePath, String destinationPath) throws Exception {
		log.debug(String.format("Azure blobstore will copy the file : %s to: %s in the container: %s", sourcePath, destinationPath, container));	
		CloudStorageAccount cloudStorageAccount = CloudStorageAccount.parse(storageConnectionString);
		CloudBlobContainer cloudContainer = cloudStorageAccount.createCloudBlobClient().getContainerReference(container);
		CloudBlockBlob source = cloudContainer.getBlockBlobReference(sourcePath);		
		CloudBlockBlob destination = cloudContainer.getBlockBlobReference(destinationPath);
		destination.startCopy(source);		
	}
}
