package at.roteskreuz.covidapp.config;

import at.roteskreuz.covidapp.properties.SignatureProperties;
import at.roteskreuz.covidapp.sign.AzureSign;
import at.roteskreuz.covidapp.sign.FilesystemSign;
import at.roteskreuz.covidapp.sign.NoopSign;
import at.roteskreuz.covidapp.sign.Sign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author roesslerb
 */
@Configuration
@Slf4j
@RequiredArgsConstructor
public class SignConfig {

	private final SignatureProperties signatureProperties;

	@Bean
	public Sign sign() {
		Sign result;
		switch (signatureProperties.getSignatureType()) {
			case AZURE: {
				log.info("Creating Azure sign");
				result = new AzureSign(signatureProperties);
				break;
			}
			case FILESYSTEM: {
				log.info("Creating filesystem sign");
				result = new FilesystemSign(signatureProperties);
				break;
			}
			default: {
				result = new NoopSign();
			}

		}
		return result;
	}
}
