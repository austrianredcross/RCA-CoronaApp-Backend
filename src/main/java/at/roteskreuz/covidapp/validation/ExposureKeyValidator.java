package at.roteskreuz.covidapp.validation;

import at.roteskreuz.covidapp.config.ApplicationConfig;
import at.roteskreuz.covidapp.model.ExposureKey;
import at.roteskreuz.covidapp.properties.PublishProperties;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Validates Exposure key objects
 *
 * @author Zoltán Puskai
 */
public class ExposureKeyValidator extends AbstractValidator implements ConstraintValidator<ValidExposureKey, ExposureKey> {
	
	@Autowired
	private PublishProperties publishProperties;
	
	
	/**
	 * Initializes the validator
	 *
	 * @param constraintAnnotation the annotation
	 */
	@Override
	public void initialize(ValidExposureKey constraintAnnotation) {
	}
	/**
	 * Validate an exposure key
	 * @param exposureKey key to be validated
	 * @param context validation context
	 * @return 
	 */
	@Override
	public boolean isValid(ExposureKey exposureKey, ConstraintValidatorContext context) {
		boolean result = true;
		LocalDateTime now = LocalDateTime.now();
		//LocalDateTime truncated = now.truncatedTo(ChronoUnit.HOURS);
		
		long minIntervalNumber = now.minus(publishProperties.getMaxIntervalAgeOnPublish()).toInstant(ZoneOffset.UTC).getEpochSecond() / ApplicationConfig.INTERVAL_LENGTH.getSeconds();
		// And have an interval <= maxInterval (configured allowed clock skew)
		long maxIntervalNumber = now.toInstant(ZoneOffset.UTC).getEpochSecond() /  ApplicationConfig.INTERVAL_LENGTH.getSeconds();
		
		String key =exposureKey.binKey();
		if (key.length() != ApplicationConfig.KEY_LENGTH) {
			addErrorMessage(context, "invalid key length, " + key.length() + ", must be " + ApplicationConfig.KEY_LENGTH);
			result = false;
			
		}
		if (exposureKey.getIntervalCount() < ApplicationConfig.MIN_INTERVAL_COUNT || exposureKey.getIntervalCount() > ApplicationConfig.MAX_INTERVAL_COUNT ) {
			addErrorMessage(context, String.format("invalid interval count, %s, must be >= %s && <= %s", exposureKey.getIntervalCount(), ApplicationConfig.MIN_INTERVAL_COUNT, ApplicationConfig.MAX_INTERVAL_COUNT));
			result = false;
		}
		if (exposureKey.getIntervalNumber() < minIntervalNumber) {
			addErrorMessage(context, String.format("interval number %s is too old, must be >= %s", exposureKey.getIntervalNumber(), minIntervalNumber));
			result = false;
		}
		if (exposureKey.getIntervalNumber() >= maxIntervalNumber) {
			addErrorMessage(context, String.format("interval number %s is in the future, must be < %s", exposureKey.getIntervalNumber(), minIntervalNumber));
			result = false;
		}		
//		if (exposureKey.getTransmissionRisk() < ApplicationConfig.MIN_TRANSMISSION_RISK || exposureKey.getTransmissionRisk() > ApplicationConfig.MAX_TRANSMISSION_RISK) {
//			addErrorMessage(context, String.format("invalid transmission risk: %s, must be >= %s && <= %s", exposureKey.getTransmissionRisk(),  ApplicationConfig.MIN_TRANSMISSION_RISK,  ApplicationConfig.MAX_TRANSMISSION_RISK));
//			result = false;
//		}		
		return result;
	}

}
