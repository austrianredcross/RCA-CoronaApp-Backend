package at.roteskreuz.covidapp.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 * @author zolika
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "application.api")
public class ApiProperties {

	private Integer version;

	
}
