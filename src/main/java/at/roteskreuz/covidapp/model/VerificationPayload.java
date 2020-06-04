package at.roteskreuz.covidapp.model;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Verification payload is used in a publish request to hold the TAN verification data
 * 
 * @author Zoltán Puskai
 */
@Getter
@Setter
@NoArgsConstructor
public class VerificationPayload {

	@NotBlank
	private String uuid;

	@NotBlank
	private String authorization;	
}
