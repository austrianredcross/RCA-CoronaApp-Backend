package at.roteskreuz.covidapp.service;

import at.roteskreuz.covidapp.model.Publish;
import java.util.Base64;
import java.util.Comparator;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author zolika
 */
@Service
@RequiredArgsConstructor
public class AndroidNonceService {
	
	private final Sha256Service sha256Service;

	public String nonce(Publish publish) {
			
		String sortedKeys = publish.getKeys().stream()
				.sorted(Comparator.comparing(c -> c.getKey()))
				.map(k-> k.getKey() + "." + k.getIntervalNumber() + "." + k.getIntervalCount() + "." + k.getTransmissionRisk())
				.collect(Collectors.joining(","));
		String sortedRegions = publish.getRegions().stream()
				.map(s -> s.toUpperCase())
				.sorted()
				.collect(Collectors.joining(","));
		
		
		StringJoiner joiner = new StringJoiner("|");
		joiner.add(publish.getAppPackageName());
		joiner.add(sortedKeys);
		joiner.add(sortedRegions);
		joiner.add(publish.getVerificationPayload());
		
		return Base64.getEncoder().encodeToString(sha256Service.sha256(joiner.toString()).getBytes());
	}	
	
}
