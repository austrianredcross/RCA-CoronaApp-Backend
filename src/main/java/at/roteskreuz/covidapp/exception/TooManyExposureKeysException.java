package at.roteskreuz.covidapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author zolika
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Too Many exposure keys")
public class TooManyExposureKeysException extends AbstractCovidException {

	public TooManyExposureKeysException(String message) {
		super(message);
	}

}
