package at.roteskreuz.covidapp.sign;

import java.io.IOException;
import java.security.GeneralSecurityException;
import org.springframework.lang.NonNull;

/**
 * Signer defines the minimum interface for a (private key) signer
 * 
 * @author Bernhard Roessler
 */
public interface Signer {

	/**
	 * Signs data
	 * @param data data to be signed
	 * @return signature
	 * @throws GeneralSecurityException
	 * @throws IOException 
	 */
	byte[] sign(@NonNull byte[] data) throws GeneralSecurityException, IOException;

}
