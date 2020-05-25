package at.roteskreuz.covidapp.properties;

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
@ConfigurationProperties(prefix = "application.publish")
public class PublishProperties {

	
	private Duration targetRequestDuration;
	private Integer maxKeysOnPublish;
	private Duration maxIntervalAgeOnPublish;
	
}
