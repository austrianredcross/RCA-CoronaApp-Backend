package at.roteskreuz.covidapp.blobstore;

/**
 * Blobstore defines the minimum interface for a blob storage system
 *
 * @author Zoltán Puskai
 */
public interface Blobstore {
	
	void createObject(String bucket, String objectName, byte[] contents)  throws Exception ;

	boolean deleteObject(String bucket, String objectName)  throws Exception ;
	
	void copy(String bucket, String sourcePath, String destinationPath) throws Exception;
	
	
}
