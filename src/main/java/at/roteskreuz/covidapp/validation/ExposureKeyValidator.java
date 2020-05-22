package at.roteskreuz.covidapp.validation;

import at.roteskreuz.covidapp.model.ExposureKey;
import at.roteskreuz.covidapp.properties.ExposureKeyProperties;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Validator for checking Exposure key objects
 *
 * @author Zoltán Puskai
 */
public class ExposureKeyValidator extends AbstractValidator implements ConstraintValidator<ValidExposureKey, ExposureKey> {
	
	@Autowired
	private ExposureKeyProperties properties;

	/**
	 * Initializes the validator
	 *
	 * @param constraintAnnotation the annotation
	 */
	@Override
	public void initialize(ValidExposureKey constraintAnnotation) {
	}
	
	@Override
	public boolean isValid(ExposureKey exposureKey, ConstraintValidatorContext context) {
		boolean result = true;
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime truncated = now.truncatedTo(ChronoUnit.HOURS);
		
		long minIntervalNumber = now.minus(properties.getMaxIntervalStartAge()).toInstant(ZoneOffset.UTC).getEpochSecond() / properties.getIntervalLength().getSeconds();
		// And have an interval <= maxInterval (configured allowed clock skew)
		long maxIntervalNumber = now.toInstant(ZoneOffset.UTC).getEpochSecond() / properties.getIntervalLength().getSeconds();
		
		String binKey =exposureKey.getBinKey();
		if (binKey.length() != properties.getKeyLength()) {
			addErrorMessage(context, "invalid key length, " + binKey.length() + ", must be " + properties.getKeyLength());
			result = false;
			
		}
		if (exposureKey.getIntervalCount() < properties.getMinIntervalCount() || exposureKey.getIntervalCount() > properties.getMaxIntervalCount()) {
			addErrorMessage(context, String.format("invalid interval count, %s, must be >= %s && <= %s", exposureKey.getIntervalCount(), properties.getMinIntervalCount(), properties.getMaxIntervalCount()));
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
		if (exposureKey.getTransmissionRisk() < properties.getMinTransmissionRisk() || exposureKey.getTransmissionRisk() > properties.getMaxTransmissionRisk()) {
			addErrorMessage(context, String.format("invalid transmission risk: %s, must be >= %s && <= %s", exposureKey.getTransmissionRisk(), properties.getMinTransmissionRisk(), properties.getMaxTransmissionRisk()));
			result = false;
		}		
		return result;
	}

}
