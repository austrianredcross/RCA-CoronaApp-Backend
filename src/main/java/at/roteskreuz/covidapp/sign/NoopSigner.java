package at.roteskreuz.covidapp.sign;

import java.io.IOException;
import java.security.GeneralSecurityException;
import lombok.extern.slf4j.Slf4j;

/**
 * Signer that does nothing.
 *
 * @author Bernhard Roessler
 */
@Slf4j
public class NoopSigner implements Signer {

	/**
	 * Does not sign data
	 * @param data data to be signed
	 * @return not signed (same) data
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	@Override
	public byte[] sign(byte[] data) throws GeneralSecurityException, IOException {
		log.info(String.format("Noop Signature don't signature data!"));
		return data;
	}

}
