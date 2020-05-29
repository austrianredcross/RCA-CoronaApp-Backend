package at.roteskreuz.covidapp.blobstore;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;

/**
 * Blobstore to store files in the filesystem
 *
 * @author Zoltán Puskai
 */
@Slf4j
public class FilesystemStorage  implements Blobstore {

	/**
	 * Creates a file in the filesystem
	 * @param folder name of the folder 
	 * @param filename name of the file to be stored
	 * @param contents data to be stored
	 * @throws Exception 
	 */	
	@Override
	public void createObject(String folder, String filename, byte[] contents) throws Exception {
		String path = folder + File.separator + filename;
		log.debug(String.format("Filesystem storage will create file: %s",path));
		File file = new File(path);
		file.getParentFile().mkdirs();
		Files.write(Paths.get(path), contents);
	}

	/**
	 * Deletes a file from the filesystem
	 * @param folder name of the folder 
	 * @param filename name of the file to be deleted
	 * @return
	 * @throws Exception 
	 */
	@Override
	public boolean deleteObject(String folder, String filename) throws Exception {
		String path = folder + File.separator + filename;
		log.debug(String.format("Filesystem storage will delete file: %s",path));
		File file = new File(path);
		return file.delete();
	}

}
