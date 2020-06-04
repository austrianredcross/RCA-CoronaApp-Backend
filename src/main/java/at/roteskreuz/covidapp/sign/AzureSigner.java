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
 * Signer that gets the private key from Azure Key Vault
 * 
 * @author Bernhard Roessler
 */
@Slf4j
@RequiredArgsConstructor
public class AzureSigner extends AbstractSigner {

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
