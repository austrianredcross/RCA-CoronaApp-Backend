package at.roteskreuz.covidapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author zolika
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Platform not supported")
public class PlatformNotSupportedException extends Exception{

}
