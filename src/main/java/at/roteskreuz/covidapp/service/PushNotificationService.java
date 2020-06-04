package at.roteskreuz.covidapp.service;

import at.roteskreuz.covidapp.properties.PushNotificationProperties;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


/**
 * Service for sending push notifications
 * 
 * @author Bernhard Roessler
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PushNotificationService {
	
	private final PushNotificationProperties pushNotificationProperties;

	/**
	 * Sends a push notification
	 * @param message message to be sent
	 * @return 
	 */
	public boolean sendNotification(String message) {
		FirebaseApp firebaseApp = getFirebaseApp();
		if(firebaseApp != null) {
			try {
				Message sendMessage = Message.builder()
						.putData("message", message)
						.setTopic(pushNotificationProperties.getFirebaseTopic())
						.build();

				String response = FirebaseMessaging.getInstance(firebaseApp).send(sendMessage);
				log.debug(String.format("FirebaseMessaging Response: %s", response));
				if (StringUtils.isNotEmpty(response)) {
					return true;
				}
			} catch (FirebaseMessagingException ex) {
				log.error("Firebase Message Error: ", ex);
			}
		} else {
			log.error("Firebase not configured. PushnotificationProperties " + pushNotificationProperties.toString() + ". GoogleCredentialsJson: " + getGoogleCredentialsJson());
		}
		return false;
	}

	private FirebaseApp getFirebaseApp() {
		if(FirebaseApp.getApps() != null && !FirebaseApp.getApps().isEmpty()){
			return FirebaseApp.getInstance();
		}

		String googleCredentialsJson = getGoogleCredentialsJson();

		if(StringUtils.isNotEmpty(googleCredentialsJson)) {
			try {
				FirebaseOptions options = new FirebaseOptions.Builder()
						.setCredentials(
								GoogleCredentials.fromStream(
										new ByteArrayInputStream(googleCredentialsJson.getBytes()))
						.createScoped(
								pushNotificationProperties.getFirebaseCloudMessageGoogleAccessTokenScope()))
						.build();

				return FirebaseApp.initializeApp(options);
			} catch (IOException ex) {
				log.error("Couldn't init FirebaseApp with Credentials", ex);
			}
		}
		return null;
	}

	private String getGoogleCredentialsJson() {
		if(StringUtils.isNotEmpty(pushNotificationProperties.getFirebaseCloudMessageType()) &&
				StringUtils.isNotEmpty(pushNotificationProperties.getFirebaseCloudMessageProjectId()) &&
				StringUtils.isNotEmpty(pushNotificationProperties.getFirebaseCloudMessagePrivateKeyId()) &&
				StringUtils.isNotEmpty(pushNotificationProperties.getFirebaseCloudMessagePrivateKey()) &&
				StringUtils.isNotEmpty(pushNotificationProperties.getFirebaseCloudMessageClientEmail()) &&
				StringUtils.isNotEmpty(pushNotificationProperties.getFirebaseCloudMessageClientId()) &&
				StringUtils.isNotEmpty(pushNotificationProperties.getFirebaseCloudMessageAuthUri()) &&
				StringUtils.isNotEmpty(pushNotificationProperties.getFirebaseCloudMessageTokenUri()) &&
				StringUtils.isNotEmpty(pushNotificationProperties.getFirebaseCloudMessageAuthProviderX509CertUrl()) &&
				StringUtils.isNotEmpty(pushNotificationProperties.getFirebaseCloudMessageClientX509CertUrl())) {
			GenericJson json = new GenericJson();
			json.setFactory(JacksonFactory.getDefaultInstance());
			json.put("type", pushNotificationProperties.getFirebaseCloudMessageType());
			json.put("project_id", pushNotificationProperties.getFirebaseCloudMessageProjectId());
			json.put("private_key_id", pushNotificationProperties.getFirebaseCloudMessagePrivateKeyId());
			json.put("private_key", pushNotificationProperties.getFirebaseCloudMessagePrivateKey());
			json.put("client_email", pushNotificationProperties.getFirebaseCloudMessageClientEmail());
			json.put("client_id", pushNotificationProperties.getFirebaseCloudMessageClientId());
			json.put("auth_uri", pushNotificationProperties.getFirebaseCloudMessageAuthUri());
			json.put("token_uri", pushNotificationProperties.getFirebaseCloudMessageTokenUri());
			json.put("auth_provider_x509_cert_url", pushNotificationProperties.getFirebaseCloudMessageAuthProviderX509CertUrl());
			json.put("client_x509_cert_url", pushNotificationProperties.getFirebaseCloudMessageClientX509CertUrl());
			return json.toString();
		}

		return null;
	}
}
