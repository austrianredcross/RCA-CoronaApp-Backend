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
 * Base class for signers.
 * Exported files are signed with private keys during the export process.
 *
 * @author Bernhard Roessler
 */
@Slf4j
public abstract class AbstractSigner implements Signer {

	/**
	 * Signs data with private key
	 * @param data data to be signed
	 * @param keyInputStream private key
	 * @param signatureAlgorithm algorithm used
	 * @param keyType key type used
	 * @return signed data
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	protected byte[] signature(byte[] data, InputStream keyInputStream, String signatureAlgorithm, String keyType)
									throws GeneralSecurityException, IOException {
		Signature ecdsa = Signature.getInstance(signatureAlgorithm);
		ecdsa.initSign(getPrivateKey(keyInputStream, keyType));
		ecdsa.update(data);
		return ecdsa.sign();
	}

	/**
	 * gets the private key used for signing data
	 * @param keyInputStream input stream to get the key from
	 * @param keyType the type of the key
	 * @return
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	protected PrivateKey getPrivateKey(InputStream keyInputStream, String keyType)
			throws IOException, GeneralSecurityException {
		return PemReader.loadPrivateKey(FileCopyUtils.copyToByteArray(keyInputStream), keyType);
	}

}
