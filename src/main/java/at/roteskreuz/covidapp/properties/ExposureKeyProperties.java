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
@ConfigurationProperties(prefix = "validation.exposure.key")
public class ExposureKeyProperties {

	private Duration maxIntervalStartAge;

	private Integer minIntervalCount;

	private Integer maxIntervalCount;

	private Integer keyLength;

	private Duration intervalLength;
	
	private Integer minTransmissionRisk;

	private Integer maxTransmissionRisk;
}
