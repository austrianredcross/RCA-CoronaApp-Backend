package at.roteskreuz.covidapp.model;

import at.roteskreuz.covidapp.validation.ValidPublish;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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
@ValidPublish
public class Publish {
	
	@Size(min = 0)
	@JsonProperty("temporaryExposureKeys")
	@Valid
	private List<ExposureKey> keys;
	
	private List<String> regions;

	private String 	appPackageName;

	private String 	platform;
	
	private String 	deviceVerificationPayload;

	private String verificationAuthorityName;
	
	private String 	padding;
	
	@NotBlank
	@Pattern(regexp = "red-warning|yellow-warning|green-warning", message = "The diagnosis type can be eighter red-warning, yellow-warning or green-warning")
	private String diagnosisType;
	
	@Valid
	@NotNull
	private VerificationPayload verificationPayload;

}
