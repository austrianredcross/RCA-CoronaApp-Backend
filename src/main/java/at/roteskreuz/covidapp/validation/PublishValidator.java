package at.roteskreuz.covidapp.validation;

import at.roteskreuz.covidapp.model.ExposureKey;
import at.roteskreuz.covidapp.model.Publish;
import java.util.Comparator;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

/**
 * Validator for checking Publish objects
 *
 * @author Zoltán Puskai
 */
public class PublishValidator extends AbstractValidator implements ConstraintValidator<ValidPublish, Publish> {

	@Value("${validation.publish.maxExposureKeys}")
	private int maxExposureKeys;

	/**
	 * Initializes the validator
	 *
	 * @param constraintAnnotation the annotation
	 */
	@Override
	public void initialize(ValidPublish constraintAnnotation) {
	}

	@Override
	public boolean isValid(Publish publish, ConstraintValidatorContext context) {
		boolean result = true;
		if (publish.getKeys().size() > maxExposureKeys) {
			addErrorMessage(context, String.format("too many exposure keys in publish:" + publish.getKeys().size() + " , max of " + maxExposureKeys + " is allowed!"));
			result= false;
		}

		// Ensure that the uploaded keys are for a consecutive time period. No
		// overlaps and no gaps.
		// 1) Sort by interval number.
		publish.getKeys().sort(Comparator.comparing(c -> c.getIntervalNumber()));

		// 2) Walk the slice and verify no gaps/overlaps.
		// We know the slice isn't empty, seed w/ the first interval.
		Integer nextInterval = publish.getKeys().get(0).getIntervalNumber();
		for (ExposureKey key : publish.getKeys()) {
			if (key.getIntervalNumber() < nextInterval) {
				addErrorMessage(context, String.format("exposure keys have overlapping intervals"));
				result= false;
			}
			nextInterval = key.getIntervalNumber() + key.getIntervalCount();
		}
		return result;
	}
	
}
