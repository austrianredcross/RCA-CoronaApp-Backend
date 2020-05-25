package at.roteskreuz.covidapp.properties;

import at.roteskreuz.covidapp.model.BlobstoreType;
import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 * @author zolika
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "application.export")
public class ExportProperties {

	private Duration createTimeout;
	private Duration workerTimeout;
	private Integer minRecords;
	private Integer maxRecords;
	private Integer paddingRange;
	private Duration truncateWindow;
	private Duration minWindowAge;
	private BlobstoreType blobstoreType = BlobstoreType.NONE;
	
	
}
