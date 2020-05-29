package at.roteskreuz.covidapp.sign;

import at.roteskreuz.covidapp.util.PemReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.Signature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileCopyUtils;

/**
 *
 * @author roesslerb
 */
@Slf4j
public abstract class AbstractSigner implements Signer {

	protected byte[] signature(byte[] data, InputStream keyInputStream, String signatureAlgorithm, String keyType)
									throws GeneralSecurityException, IOException {
		Signature ecdsa = Signature.getInstance(signatureAlgorithm);
		ecdsa.initSign(getPrivateKey(keyInputStream, keyType));
		ecdsa.update(data);
		return ecdsa.sign();
	}

	protected PrivateKey getPrivateKey(InputStream keyInputStream, String keyType)
			throws IOException, GeneralSecurityException {
		return PemReader.loadPrivateKey(FileCopyUtils.copyToByteArray(keyInputStream), keyType);
	}

}
