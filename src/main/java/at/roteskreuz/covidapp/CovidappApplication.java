package at.roteskreuz.covidapp;

import at.roteskreuz.covidapp.properties.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Class for running the application.
 *
 * @author Zoltán Puskai
 */
@SpringBootApplication
@EnableConfigurationProperties({
	ApiProperties.class, 
	ExportProperties.class, 
	PublishProperties.class, 
	SignatureProperties.class,
	PushNotificationProperties.class
})
public class CovidappApplication {

	/**
	 * Runs the application using the
	 * {@link SpringApplication}.
	 * @param args the application arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(CovidappApplication.class, args);
	}

}
