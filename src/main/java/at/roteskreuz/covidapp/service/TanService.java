package at.roteskreuz.covidapp.service;

import at.roteskreuz.covidapp.model.TanRequest;
import io.micrometer.core.instrument.util.StringUtils;
import java.util.StringJoiner;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service for interacting with the external TAN service.
 * external personal data strage: url, authorization key, authorization value, sha256 key can be configured in the application.properties
 * 
 * @author Zoltán Puskai
 * @since 0.0.1-SNAPSHOT
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class TanService {

	private final RestTemplate restTemplate;
	
	private final Sha256Service sha256Service;
		
	private final Pattern TAN_PATTERN = Pattern.compile("^[0-9]{6}$");
	
	@Value("${external.personal.data.storage.url:}")
	private String personalDataServiceUrl;
	
	@Value("${external.personal.data.storage.authorization.key.name:}")
	private String personalDataServiceAutorizationKeyName;

		
	@Value("${external.personal.data.storage.authorization.key.value:}")
	private String personalDataServiceAutorizationKeyValue;
	

	@Value("${external.personal.data.storage.sha256.key:}")
	private String personalDataServiceSha256Key;
	
	/**
	 * Validates a phone number with a TAN
	 * 
	 * @param uuid id of the request
	 * @param tan TAN
	 * @param type
	 * @return returns a response that contain the state and the response from the external TAN service
	 */
	public boolean validate(String uuid, String tan, String type) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		headers.add(personalDataServiceAutorizationKeyName,personalDataServiceAutorizationKeyValue);
		
		StringJoiner stringJoiner = new StringJoiner(",");
		stringJoiner.add(uuid).add(personalDataServiceSha256Key).add(type);		
		String sha256 = sha256Service.sha256(stringJoiner.toString());		
		TanRequest request = new TanRequest(uuid, tan, type, sha256);
		HttpEntity<TanRequest> entity= new HttpEntity<>(request, headers);
		if (StringUtils.isBlank(tan) || !TAN_PATTERN.matcher(tan).matches()) {
			// The TAN did not match the expected pattern --> fail
			// NOTE: This should not be a silent fail, thus validation annotation cannot be used
			return false;
		} else {
			try {
				ResponseEntity<String> tanCall = restTemplate.postForEntity(personalDataServiceUrl, entity, String.class);
				return tanCall.getStatusCodeValue() == 200;
				//return new TanResponse(tanCall.getStatusCodeValue(), tanCall.getBody());
			} catch (Exception e) {
				//return new TanResponse(e.getRawStatusCode(), INVALID_TAN_ERROR_MESSAGE);
				return false;
			}
		}
		
	}

}
