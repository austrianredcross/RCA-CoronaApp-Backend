package at.roteskreuz.covidapp.sign;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 *
 * @author roesslerb
 */
@Slf4j
public class NoopSign implements Sign {

	@Override
	public byte[] signature(byte[] data) throws GeneralSecurityException, IOException {
		log.info(String.format("Noop Signature don't signature data!"));
		return data;
	}

}
