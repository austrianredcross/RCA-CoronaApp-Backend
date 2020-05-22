package at.roteskreuz.covidapp.validation;

import javax.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author zolika
 */
@Slf4j
public class AbstractValidator  {
	
	public void addErrorMessage(ConstraintValidatorContext context, String message) {
		log.error(message);
		context.disableDefaultConstraintViolation();
		context
				.buildConstraintViolationWithTemplate(message)
				.addConstraintViolation();
	}	
}
