package at.roteskreuz.covidapp.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Represents API related external configuration
 *
 * @author Zoltán Puskai
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "application.api")
public class ApiProperties {

	private Integer version;


}
