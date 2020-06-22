package at.roteskreuz.covidapp.exception;

/**
 * Base class for exceptions used in this application
 *
 * @author Zoltán Puskai
 */
public class AbstractCovidException extends Exception {

	public AbstractCovidException(String message) {
		super(message);
	}
}
