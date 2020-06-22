package at.roteskreuz.covidapp.sign;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Signer defines the minimum interface for a (private key) signer
 *
 * @author Bernhard Roessler
 */
public interface Signer {

	/**
	 * Signs data
	 * @param data data to be signed
	 * @return signed data
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	byte[] sign(byte[] data) throws GeneralSecurityException, IOException;

}
