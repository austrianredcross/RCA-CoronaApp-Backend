package at.roteskreuz.covidapp.sign;

import at.roteskreuz.covidapp.properties.SignatureProperties;
import java.io.IOException;
import java.security.GeneralSecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author roesslerb
 */
@Slf4j
@RequiredArgsConstructor
public class FilesystemSigner extends AbstractSigner {

	private final SignatureProperties signatureProperties;

	@Override
	public byte[] signature(byte[] data) throws GeneralSecurityException, IOException {
		return signature(data, new ClassPathResource(signatureProperties.getFilePrivateKeyLocation()).getInputStream(),
							signatureProperties.getSignatureAlgorithm(), signatureProperties.getSignatureKeyType());
	}
}
