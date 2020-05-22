package at.roteskreuz.covidapp.validation;

import java.lang.annotation.*;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Annotation for validating phone numbers
 * 
 * @author Zoltán Puskai
 */

@Documented
@Constraint(validatedBy = PublishValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPublish {

	String message() default "It is not a valid Publish object";
	
	public String value() default "";
	
	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
	
}
