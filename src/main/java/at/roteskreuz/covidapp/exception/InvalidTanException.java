package at.roteskreuz.covidapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author zolika
 */
@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "TAN is invalid")
public class InvalidTanException extends AbstractCovidException {

	public InvalidTanException(String message) {
		super(message);
	}

}
