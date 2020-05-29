package at.roteskreuz.covidapp.model;


import lombok.Value;

/**
 * Response of a TAN validation request
 * 
 * @author Zoltán Puskai
 */
@Value
public class TanResponse {
	private final int httpCode;
	private final String message;
}
