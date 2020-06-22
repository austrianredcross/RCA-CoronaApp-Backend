package at.roteskreuz.covidapp.config;

import at.roteskreuz.covidapp.blobstore.AzureBlobstore;
import at.roteskreuz.covidapp.blobstore.Blobstore;
import at.roteskreuz.covidapp.blobstore.FilesystemStorage;
import at.roteskreuz.covidapp.blobstore.NoopBlobstore;
import at.roteskreuz.covidapp.properties.ExportProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Class that configures the blobstore
 *
 * @author Zoltán Puskai
 */
@Configuration
@Slf4j
@RequiredArgsConstructor
public class BlobstoreConfig {

	private final ExportProperties exportProperties;

	/**
	 * Instantiates a blobstore
	 * @return blobstore according to the configuration
	 */
	@Bean
	public Blobstore blobstore() {

		Blobstore result;
		switch (exportProperties.getBlobstoreType()) {
			case AZURE_CLOUD_STORAGE: {
				log.info("Creating Azure blobstore");
				result = new AzureBlobstore();
				break;
			}
			case FILESYSTEM: {
				log.info("Creating filesystem blobstore");
				result = new FilesystemStorage();
				break;
			}
			case NONE: {
				log.info("Creating noop blobstore");
				result = new NoopBlobstore();
				break;
			}
			default: {
				result = new NoopBlobstore();
			}

		}
		return result;
	}
}
