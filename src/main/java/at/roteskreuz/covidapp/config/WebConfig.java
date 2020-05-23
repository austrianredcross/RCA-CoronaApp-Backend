package at.roteskreuz.covidapp.config;

import javax.servlet.Filter;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

/**
 * Web configuration 
 *
 * @author Zoltán Puskai
 * @since 0.0.1-SNAPSHOT
 */

@Configuration
public class WebConfig {

	
	/**
	 * Creates an ETag filter
	 *
	 * @return ETag filter
	 */				
	@Bean
	public Filter shallowEtagHeaderFilter() {
		return new ShallowEtagHeaderFilter();
	}
	
	/**
	 * Creates a new RestTemplate
	 *
	 * @param builder the builder used to create the template
	 * @return configured RestTemplate
	 */		
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}	
	
}
