package at.roteskreuz.covidapp.blobstore;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author zolika
 */
@Slf4j
public class NoopBlobstore implements Blobstore {

	@Override
	public void createObject(String bucket, String objectName, byte[] contents) throws Exception {
		log.info(String.format("Noop blobstore will not any create file for bucket: %s and objectName: %s",bucket, objectName));
	}

	@Override
	public boolean deleteObject(String bucket, String objectName) throws Exception {
		log.info(String.format("Noop blobstore will not delete any file for bucket: %s and objectName: %s",bucket, objectName));
		return true;
	}

}
