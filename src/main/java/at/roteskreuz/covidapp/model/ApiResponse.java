package at.roteskreuz.covidapp.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Standard API response
 * 
 * @author Zoltán Puskai
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {

	/**
	 * Constructor to build the response
	 * 
	 * @param status HTTP status
	 * @param error error message
	 * @param message detailed message
	 * @param path context path of the called API endpoint
	 */
	public ApiResponse(int status, String error, String message, String path) {
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
		this.timestamp = LocalDateTime.now();
	}
	
	private int status;
	private String error;
	private String message;
	private String path;
	private LocalDateTime timestamp;

	/**
	 * OK response. Used when the request was processed well.
	 * 
	 * @return OK response with HTTP status 200
	 */
	public static ApiResponse ok() {
		return new ApiResponse(200, null, "OK", null);
	}
	
}
