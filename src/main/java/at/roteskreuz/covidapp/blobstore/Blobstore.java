package at.roteskreuz.covidapp.blobstore;

/**
 * Blobstore interface
 *
 * @author Zoltán Puskai
 */
public interface Blobstore {
	
	void createObject(String bucket, String objectName, byte[] contents)  throws Exception ;

	boolean deleteObject(String bucket, String objectName)  throws Exception ;
}
