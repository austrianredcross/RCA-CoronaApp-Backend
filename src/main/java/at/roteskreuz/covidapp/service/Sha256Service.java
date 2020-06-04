package at.roteskreuz.covidapp.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.stereotype.Service;

/**
 * Service class to create SHA256 hash
 *
 * @author Zoltán Puskai
 */
@Service
public class Sha256Service {

	/**
	 * Creates a SHA256 hash for the given key
	 * @param key key for hashing
	 * @return hash
	 */
	public byte[] sha256(byte[] key) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			return digest.digest(key);
		} catch (NoSuchAlgorithmException e) {
		}
		return null;
	}
	
	/**
	 * Creates a SHAA256 hash for the given key
	 * @param key key for hashing
	 * @return hash
	 */
	public String sha256(String key) {
		return bytesToHex(sha256(key.getBytes(StandardCharsets.UTF_8)));
	}
	
	

	private static String bytesToHex(byte[] hash) {
		if (hash == null) {
			return null;
		}
		StringBuilder hexString = new StringBuilder();
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}

}
