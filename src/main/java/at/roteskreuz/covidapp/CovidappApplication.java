package at.roteskreuz.covidapp;

import at.roteskreuz.covidapp.domain.Exposure;
import at.roteskreuz.covidapp.properties.*;
import at.roteskreuz.covidapp.repository.ExposureRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
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
@Slf4j
public class CovidappApplication implements CommandLineRunner {

	/**
	 * Runs the application using the {@link SpringApplication}.
	 *
	 * @param args the application arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(CovidappApplication.class, args);
	}

	@Autowired
	private ExposureRepository exposureRepository;

	private void createExposures(int count) {
		if (count > 0) {

			log.info(String.format("Creating %d exposures", count));
			Random random = new Random();

			List<String> diagnosisTypes = Arrays.asList("red-warning", "yellow-warning", "red-warning", "yellow-warning", "green-warning");
			LocalDateTime now = LocalDateTime.now();

			for (int i = 0; i < count; i++) {
				String diagnosisType = diagnosisTypes.get(random.nextInt(diagnosisTypes.size()));
				String exposureKey = randomString(16);
				String password = randomString(8);
				String appPackageName = "at.roteskreuz.stopcorona.stage";
				String regions = ",AT,HU,";
				Integer intervalNumber = random.nextInt(2160) + 2650038;
				Integer intervalCount = random.nextInt(10);
				LocalDateTime created = now.minusMinutes(random.nextInt(2000));
				Exposure exposure = new Exposure(exposureKey, password, appPackageName, regions, intervalNumber, intervalCount, created, false, null, diagnosisType, null);
				exposureRepository.save(exposure);
				if (i % 10000 == 0) {
					log.info("Processed: " + i);
				}
			}
			log.info(count + " exposures created");
		}

	}

	private String randomString(int length) {
		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		Random random = new Random();
		StringBuilder buffer = new StringBuilder(length);
		for (int i = 0; i <= length; i++) {
			int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
			buffer.append((char) randomLimitedInt);
		}
		return buffer.toString();
	}

	@Override
	public void run(String... args) throws Exception {
		createExposures(0);
	}
}
