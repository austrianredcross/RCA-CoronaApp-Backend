package at.roteskreuz.covidapp;

import at.roteskreuz.covidapp.properties.ExposureKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ExposureKeyProperties.class)
public class CovidappApplication {

	public static void main(String[] args) {
		SpringApplication.run(CovidappApplication.class, args);
	}

}
