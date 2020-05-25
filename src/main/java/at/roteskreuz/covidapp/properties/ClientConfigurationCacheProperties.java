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
@ConfigurationProperties(prefix = "client.configuration.cache")
public class ClientConfigurationCacheProperties {

	private Integer ttl; //ttl in miliseconds
}
