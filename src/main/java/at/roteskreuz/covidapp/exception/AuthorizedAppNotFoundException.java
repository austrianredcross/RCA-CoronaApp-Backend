package at.roteskreuz.covidapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author zolika
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Unauthorized app")
public class AuthorizedAppNotFoundException extends Exception{

}
