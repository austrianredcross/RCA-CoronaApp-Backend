package at.roteskreuz.covidapp.sign;

import java.io.IOException;
import java.security.GeneralSecurityException;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author roesslerb
 */
@Slf4j
public class NoopSigner implements Signer {

	@Override
	public byte[] signature(byte[] data) throws GeneralSecurityException, IOException {
		log.info(String.format("Noop Signature don't signature data!"));
		return data;
	}

}
