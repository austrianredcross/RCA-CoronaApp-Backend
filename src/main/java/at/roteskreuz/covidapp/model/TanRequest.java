package at.roteskreuz.covidapp.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request to validate a TAN with a phone number
 * 
 * @author Zoltán Puskai
 * @since 0.0.1-SNAPSHOT
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TanRequest {

	
	private String uuid; 

	//@Pattern(regexp = "^[0-9]{6}$", message = "Should be a valid TAN")	
	private  String tan;
	
	//@Pattern(regexp = "red-warning|yellow-warning|green-warning", message = "The type can be eighter red-warning, yellow-warning or green-warning")
	private String type;
	
	private String hash;	
}
