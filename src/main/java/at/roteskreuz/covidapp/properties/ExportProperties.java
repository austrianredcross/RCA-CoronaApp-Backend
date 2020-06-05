package at.roteskreuz.covidapp.properties;

import at.roteskreuz.covidapp.model.BlobstoreType;
import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Represents export related external configuration
 * 
 * @author Zoltán Puskai
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "application.export")
public class ExportProperties {

	private Duration createTimeout;
	private Duration workerTimeout;
	private Integer minRecords;
	private Integer maxRecords = Integer.MAX_VALUE;
	private Integer paddingRange;
	private Duration truncateWindow;
	private Duration minWindowAge;
	private BlobstoreType blobstoreType = BlobstoreType.NONE;
	private boolean exportCurrentDay;

	
	
}
