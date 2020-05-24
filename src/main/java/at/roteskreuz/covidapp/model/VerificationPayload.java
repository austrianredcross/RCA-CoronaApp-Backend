package at.roteskreuz.covidapp.model;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author zolika
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
