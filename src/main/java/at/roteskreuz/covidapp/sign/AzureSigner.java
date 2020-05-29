package at.roteskreuz.covidapp.sign;

import at.roteskreuz.covidapp.properties.SignatureProperties;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author roesslerb
 */
@Slf4j
@RequiredArgsConstructor
public class AzureSigner extends AbstractSigner {

	private final SignatureProperties signatureProperties;

	@Override
	public byte[] signature(byte[] data) throws GeneralSecurityException, IOException {
		String secret = getAzureSecret();
		return signature(data, new ByteArrayInputStream(secret.getBytes()),
							signatureProperties.getSignatureAlgorithm(), signatureProperties.getSignatureKeyType());
	}

	private String getAzureSecret() {
		SecretClient secretClient = new SecretClientBuilder()
				.vaultUrl("https://" + signatureProperties.getAzureKeyVaultName() + ".vault.azure.net/")
				.credential(new DefaultAzureCredentialBuilder().build())
				.buildClient();
		return secretClient.getSecret(signatureProperties.getAzureSecretName()).getValue();
	}
}
