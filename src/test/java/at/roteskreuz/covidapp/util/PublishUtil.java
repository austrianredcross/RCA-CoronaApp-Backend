package at.roteskreuz.covidapp.util;

import at.roteskreuz.covidapp.config.ApplicationConfig;
import at.roteskreuz.covidapp.model.ExposureKey;
import at.roteskreuz.covidapp.model.Publish;
import at.roteskreuz.covidapp.model.VerificationPayload;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 *
 * @author zolika
 */
public class PublishUtil {
	
	
	public static Publish createPublish(int exposureKeysCount) {
		Publish publish = new Publish();
		publish.setAppPackageName("at.roteskreuz.stopcorona.stage");
		publish.setPlatform("android");
		Random random = new Random();
		List<String> diagnosisTypes = Arrays.asList("red-warning", "yellow-warning", "red-warning", "yellow-warning", "green-warning");
		String diagnosisType = diagnosisTypes.get(random.nextInt(diagnosisTypes.size()));
		publish.setDiagnosisType(diagnosisType);
		publish.setRegions(Arrays.asList("AT"));
		LocalDateTime now = LocalDateTime.now().minusMinutes(15);
		publish.setKeys(createExposureKeys(diagnosisType, exposureKeysCount));
		publish.setVerificationAuthorityName("RedCross");
		VerificationPayload verificationPayload = new VerificationPayload();
		verificationPayload.setAuthorization(randomString(12));
		verificationPayload.setUuid(UUID.randomUUID().toString());
		publish.setVerificationPayload(verificationPayload);		
		return publish;
	} 
	
	private static List<ExposureKey> createExposureKeys(String diagnosisType, int count) {
		List<ExposureKey> result = new ArrayList<>();
		if (count > 0) {
			Random random = new Random();
			LocalDateTime time = LocalDateTime.now().minusMinutes(15);
			long intervalNumber = time.toInstant(ZoneOffset.UTC).getEpochSecond() / ApplicationConfig.INTERVAL_LENGTH.getSeconds();
			for (int i = 0; i < count; i++) {
				String exposureKey = new String(Base64.getEncoder().encode(randomString(16).getBytes()));
				String password = randomString(8);
				Integer intervalCount = random.nextInt(10);
				ExposureKey key = new ExposureKey(exposureKey, (int)intervalNumber, intervalCount, password);
				result.add(key);
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
