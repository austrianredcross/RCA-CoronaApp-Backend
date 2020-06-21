package at.roteskreuz.covidapp.blobstore;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import lombok.extern.slf4j.Slf4j;

/**
 * FilesystemStorage implements Blobstore and provides the ability write files
 * to the file-system.
 *
 * @author Zoltán Puskai
 */
@Slf4j
public class FilesystemStorage implements Blobstore {

	/**
	 * Creates a file in the file-system
	 *
	 * @param folder name of the folder
	 * @param filename name of the file to be stored
	 * @param contents data to be stored
	 * @throws Exception
	 */
	@Override
	public void createObject(String folder, String filename, byte[] contents) throws Exception {
		String path = folder + File.separator + filename;
		log.debug(String.format("Filesystem storage will create file: %s", path));
		File file = new File(path);
		file.getParentFile().mkdirs();
		Files.write(Paths.get(path), contents);
	}

	/**
	 * Deletes a file from the file-system
	 *
	 * @param folder name of the folder
	 * @param filename name of the file to be deleted
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean deleteObject(String folder, String filename) throws Exception {
		String path = folder + File.separator + filename;
		log.debug(String.format("Filesystem storage will delete file: %s", path));
		File file = new File(path);
//		if (file.isDirectory()) {
//			Files.walk(Paths.get(path))
//					.sorted(Comparator.reverseOrder())
//					.map(Path::toFile)
//					.forEach(File::delete);
//			return true;
//		} else {
			if (file.exists()) {
				File parentDir = file.getParentFile();
				boolean result = file.delete();
				if (parentDir.listFiles().length == 0) {
					parentDir.delete();
				}
				return result;
			}
			return false;
//		}
	}



	/**
	 * Copies a file (and replaces if destination exists)
	 *
	 * @param folder name of the folder
	 * @param sourceFileName name of the source file
	 * @param destinationFileName name of the destination file
	 * @throws Exception
	 */
	@Override
	public void copy(String folder, String sourceFileName, String destinationFileName) throws Exception {
		log.debug(String.format("Filesystem storage will copy the file : %s to: %s", folder + File.separator + sourceFileName, folder + File.separator + destinationFileName));
		Files.copy(Paths.get(folder + File.separator + sourceFileName), Paths.get(folder + File.separator + destinationFileName), StandardCopyOption.REPLACE_EXISTING);
	}

}
