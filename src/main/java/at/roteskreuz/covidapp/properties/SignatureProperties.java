package at.roteskreuz.covidapp.properties;

import at.roteskreuz.covidapp.model.SignatureType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * Represents  signature related external configuration
 * 
 * @author Zoltán Puskai
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "application.signature")
public class SignatureProperties {

	private SignatureType signatureType = SignatureType.NONE;
	private String signatureAlgorithm = "SHA256withECDSA";
	private String signatureKeyType = "EC";
	private String azureKeyVaultName;
	private String azureSecretName;
	private String filePrivateKeyLocation;

}
