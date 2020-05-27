package at.roteskreuz.covidapp.blobstore;

/**
 *
 * @author zolika
 */
public interface Blobstore {
	void createObject(String bucket, String objectName, byte[] contents)  throws Exception ;

	boolean deleteObject(String bucket, String objectName)  throws Exception ;
}
