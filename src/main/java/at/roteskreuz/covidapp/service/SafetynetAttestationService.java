package at.roteskreuz.covidapp.service;

import org.springframework.stereotype.Service;

/**
 *
 * @author zolika
 */
@Service
public class SafetynetAttestationService {
	
	public boolean isAttestationValid(String attestation) {
		return true;
	}

}
