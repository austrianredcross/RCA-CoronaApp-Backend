package at.roteskreuz.covidapp.validation;

import java.lang.annotation.*;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Annotation for validating exposure keys
 *
 * @author Zoltán Puskai
 */
@Documented
@Constraint(validatedBy = ExposureKeyValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidExposureKey {

	String message() default "It is not a valid ExposureKey object";

	public String value() default "";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
