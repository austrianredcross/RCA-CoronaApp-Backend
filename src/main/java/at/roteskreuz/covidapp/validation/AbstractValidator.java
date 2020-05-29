package at.roteskreuz.covidapp.validation;

import javax.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for validators
 *
 * @author Zoltán Puskai
 */
@Slf4j
public class AbstractValidator  {
	
	/**
	 * Adds an error message to the response
	 * @param context validation context
	 * @param message message
	 */
	public void addErrorMessage(ConstraintValidatorContext context, String message) {
		log.error(message);
		context.disableDefaultConstraintViolation();
		context
				.buildConstraintViolationWithTemplate(message)
				.addConstraintViolation();
	}	
}
