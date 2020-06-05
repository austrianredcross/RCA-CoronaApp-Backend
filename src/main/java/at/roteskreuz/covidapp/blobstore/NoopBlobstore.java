package at.roteskreuz.covidapp.blobstore;

import lombok.extern.slf4j.Slf4j;

/**
 * Blobstore that does nothing.
 *
 * @author Zoltán Puskai
 */
@Slf4j
public class NoopBlobstore implements Blobstore {

	/**
	 * This method does not store any object
	 * @param bucket
	 * @param objectName
	 * @param contents
	 * @throws Exception 
	 */
	@Override
	public void createObject(String bucket, String objectName, byte[] contents) throws Exception {
		log.info(String.format("Noop blobstore will not any create file for bucket: %s and objectName: %s",bucket, objectName));
	}

	/**
	 * This method does not delete any object
	 * @param bucket
	 * @param objectName
	 * @return
	 * @throws Exception 
	 */
	@Override
	public boolean deleteObject(String bucket, String objectName) throws Exception {
		log.info(String.format("Noop blobstore will not delete any file for bucket: %s and objectName: %s",bucket, objectName));
		return true;
	}
	
	/**
	 * This method does not copy anything
	 * @param bucket
	 * @param sourcePath
	 * @param destinationPath
	 * @throws Exception 
	 */
	public void copy(String bucket, String sourcePath, String destinationPath) throws Exception {
		log.info(String.format("Noop blobstore will not copy any file for bucket: %s",bucket));
	}

}
