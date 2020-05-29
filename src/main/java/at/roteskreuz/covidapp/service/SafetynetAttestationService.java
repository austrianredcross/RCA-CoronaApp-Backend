package at.roteskreuz.covidapp.service;

import org.springframework.stereotype.Service;

/**
 * Service checks if the device is an Android device using Safetynet attestation
 * 
 * @author Zoltán Puskai
 */
@Service
public class SafetynetAttestationService {
	
	/**
	 * Implementation missing
	 * @param attestation attestation to be checked
	 * @return 
	 */	
	public boolean isAttestationValid(String attestation) {
		return true;
	}

}