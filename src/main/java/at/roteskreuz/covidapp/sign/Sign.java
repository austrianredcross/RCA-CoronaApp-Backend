package at.roteskreuz.covidapp.sign;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 *
 * @author roesslerb
 */
public interface Sign {

	byte[] signature(byte[] data) throws GeneralSecurityException, IOException;

}
