package at.roteskreuz.covidapp.sign;

import at.roteskreuz.covidapp.properties.SignatureProperties;
import java.io.IOException;
import java.security.GeneralSecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

/**
 * Signer that gets the private key from filesystem.
 * 
 * @author Bernhard Roessler
 */

@Slf4j
@RequiredArgsConstructor
public class FilesystemSigner extends AbstractSigner {

	private final SignatureProperties signatureProperties;

	/**
	 * Signs data
	 * @param data data to be signed
	 * @return signed data
	 * @throws GeneralSecurityException
	 * @throws IOException 
	 */
	@Override
	public byte[] sign(byte[] data) throws GeneralSecurityException, IOException {
		return signature(data, new ClassPathResource(signatureProperties.getFilePrivateKeyLocation()).getInputStream(),
							signatureProperties.getSignatureAlgorithm(), signatureProperties.getSignatureKeyType());
	}
}
