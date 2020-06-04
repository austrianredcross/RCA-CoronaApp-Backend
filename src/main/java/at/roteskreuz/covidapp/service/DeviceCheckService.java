package at.roteskreuz.covidapp.service;

import org.springframework.stereotype.Service;

/**
 * Service checks if the device is an IOS device
 * 
 * @author Zoltán Puskai
 */
@Service
public class DeviceCheckService {
	/**
	 * Implementation missing
	 * @param token token to be checked
	 * @return 
	 */
	public boolean isDeviceTokenValid(String token) {
		return true;
	}
}
