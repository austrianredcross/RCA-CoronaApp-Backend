package at.roteskreuz.covidapp.sign;

import at.roteskreuz.covidapp.properties.SignatureProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 *
 * @author roesslerb
 */
@Slf4j
@RequiredArgsConstructor
public class FilesystemSign extends AbstractSign {

	private final SignatureProperties signatureProperties;

	@Override
	public byte[] signature(byte[] data) throws GeneralSecurityException, IOException {
		return signature(data, new ClassPathResource(signatureProperties.getFilePrivateKeyLocation()).getInputStream(),
							signatureProperties.getSignatureAlgorithm(), signatureProperties.getSignatureKeyType());
	}
}
