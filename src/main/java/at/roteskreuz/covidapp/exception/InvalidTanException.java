package at.roteskreuz.covidapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when TAN is invalid
 *
 * @author Zoltán Puskai
 */
@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "TAN is invalid")
public class InvalidTanException extends AbstractCovidException {

	public InvalidTanException(String message) {
		super(message);
	}

}
