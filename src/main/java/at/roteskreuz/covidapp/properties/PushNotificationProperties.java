package at.roteskreuz.covidapp.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * Represents  push notification related external configuration
 * 
 * @author Bernhard Roessler
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
