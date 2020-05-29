package at.roteskreuz.covidapp.config;

import at.roteskreuz.covidapp.properties.SignatureProperties;
import at.roteskreuz.covidapp.sign.AzureSigner;
import at.roteskreuz.covidapp.sign.FilesystemSigner;
import at.roteskreuz.covidapp.sign.NoopSigner;
import at.roteskreuz.covidapp.sign.Signer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Class for configuring the Signer used for signing exported files
 *
 * @author Bernhard Roessler
 */
@Configuration
@Slf4j
@RequiredArgsConstructor
public class SignerConfig {

	private final SignatureProperties signatureProperties;

	/**
	 * Creates a signer
	 * @return signer according to current configuration
	 */
	@Bean
	public Signer signer() {
		Signer result;
		switch (signatureProperties.getSignatureType()) {
			case AZURE: {
				log.info("Creating Azure sign");
				result = new AzureSigner(signatureProperties);
				break;
			}
			case FILESYSTEM: {
				log.info("Creating filesystem sign");
				result = new FilesystemSigner(signatureProperties);
				break;
			}
			default: {
				result = new NoopSigner();
			}

		}
		return result;
	}
}
