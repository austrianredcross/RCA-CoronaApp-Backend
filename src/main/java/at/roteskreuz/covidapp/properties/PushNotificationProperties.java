package at.roteskreuz.covidapp.properties;

import at.roteskreuz.covidapp.model.SignatureType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 *
 * @author roesslerb
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "application.pushnotification")
public class PushNotificationProperties {

	private String firebaseTopic;
	private String firebaseCloudMessageGoogleAccessTokenScope;
	private String firebaseCloudMessageType;
	private String firebaseCloudMessageProjectId;
	private String firebaseCloudMessagePrivateKeyId;
	private String firebaseCloudMessagePrivateKey;
	private String firebaseCloudMessageClientEmail;
	private String firebaseCloudMessageClientId;
	private String firebaseCloudMessageAuthUri;
	private String firebaseCloudMessageTokenUri;
	private String firebaseCloudMessageAuthProviderX509CertUrl;
	private String firebaseCloudMessageClientX509CertUrl;

}
