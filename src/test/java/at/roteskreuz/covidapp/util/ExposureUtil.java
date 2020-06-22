package at.roteskreuz.covidapp.util;

import at.roteskreuz.covidapp.config.ApplicationConfig;
import at.roteskreuz.covidapp.domain.Exposure;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Random;

/**
 *
 * @author zolika
 */
public class ExposureUtil {

	public static List<Exposure> createExposures(int count) {
		List<Exposure> result = new ArrayList<>();
		if (count > 0) {

			Random random = new Random();

			List<String> diagnosisTypes = Arrays.asList("red-warning", "yellow-warning", "red-warning", "yellow-warning", "green-warning");
			LocalDateTime now = LocalDateTime.now();

			for (int i = 0; i < count; i++) {
				String diagnosisType = diagnosisTypes.get(random.nextInt(diagnosisTypes.size()));
				String exposureKey = new String(Base64.getEncoder().encode(randomString(16).getBytes()));
				String password = randomString(8);
				String appPackageName = "at.roteskreuz.stopcorona.stage";
				String regions = ",AT,HU,";
				long intervalNumberStart = LocalDateTime.now().minusDays(14).toInstant(ZoneOffset.UTC).getEpochSecond() / ApplicationConfig.INTERVAL_LENGTH.getSeconds();
				Integer intervalNumber = random.nextInt((int) intervalNumberStart) + 2650038;
				Integer intervalCount = random.nextInt(10);
				LocalDateTime created = now.minusMinutes(random.nextInt(2000));
				Exposure exposure = new Exposure(exposureKey, password, appPackageName, regions, intervalNumber, intervalCount, created, false, null, diagnosisType, null);
				result.add(exposure);
			}
		}
		return result;
	}

	private static String randomString(int length) {
		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		Random random = new Random();
		StringBuilder buffer = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
			buffer.append((char) randomLimitedInt);
		}
		return buffer.toString();
	}
}
