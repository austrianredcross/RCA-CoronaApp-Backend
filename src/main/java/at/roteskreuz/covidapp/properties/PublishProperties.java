package at.roteskreuz.covidapp.properties;

import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Represents  publish related external configuration
 *
 * @author Zoltán Puskai
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "application.publish")
public class PublishProperties {


	private Duration targetRequestDuration;
	private Integer maxKeysOnPublish;
	private Duration maxIntervalAgeOnPublish;
	private boolean bypassTanValidation;


}
