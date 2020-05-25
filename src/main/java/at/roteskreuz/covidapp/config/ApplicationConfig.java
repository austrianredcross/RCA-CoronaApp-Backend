package at.roteskreuz.covidapp.config;

import at.roteskreuz.covidapp.properties.PublishProperties;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

/**
 *
 * @author zolika
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
	
	
	public ApplicationConfig(PublishProperties publishProperties) {
		int maxExposureKeys = publishProperties.getMaxKeysOnPublish();
		Assert.isTrue((maxExposureKeys>= 0 && maxExposureKeys <= MAX_KEYS_PER_PUBLISH), String.format("maxExposureKeys must be > 0 and <= %s, got %s", MAX_KEYS_PER_PUBLISH, maxExposureKeys));
	}
	
	
	// 21 Days worth of keys is the maximum per publish request (inclusive)
	public static final Integer MAX_KEYS_PER_PUBLISH  = 21;

	// only valid exposure key keyLength
	public static final Integer KEY_LENGTH = 16;

	// Transmission risk constraints (inclusive..inclusive)
	public static final Integer MIN_TRANSMISSION_RISK = 0; // 0 indicates, no/unknown risk.
	public static final Integer MAX_TRANSMISSION_RISK =8;

	// Intervals are defined as 10 minute periods, there are 144 of them in a day.
	// IntervalCount constraints (inclusive..inclusive)
	public static final Integer MIN_INTERVAL_COUNT = 1;
	public static final Integer MAX_INTERVAL_COUNT = 144;
	// Self explanatory.
	// oneDay = time.Hour * 24

	// interval length
	public static final Duration INTERVAL_LENGTH = Duration.ofMinutes(10);

	
	
}
