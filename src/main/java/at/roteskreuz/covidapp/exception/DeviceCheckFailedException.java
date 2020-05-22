package at.roteskreuz.covidapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author zolika
 */
@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Safetynet attestation failed")
public class DeviceCheckFailedException extends Exception {

}
