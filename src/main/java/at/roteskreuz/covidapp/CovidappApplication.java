package at.roteskreuz.covidapp;

import at.roteskreuz.covidapp.properties.ApiProperties;
import at.roteskreuz.covidapp.properties.ClientConfigurationCacheProperties;
import at.roteskreuz.covidapp.properties.ExportProperties;
import at.roteskreuz.covidapp.properties.PublishProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ApiProperties.class,ClientConfigurationCacheProperties.class, ExportProperties.class,PublishProperties.class})
public class CovidappApplication {

	public static void main(String[] args) {
		SpringApplication.run(CovidappApplication.class, args);
	}

}
