package at.roteskreuz.covidapp.service;

import at.roteskreuz.covidapp.properties.SignatureProperties;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.security.*;

/**
 *
 * @author zolika
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SignatureService {
	private final SignatureProperties signatureProperties;

	public byte[] getSignature(byte[] data) {
		try {
			switch (signatureProperties.getSignatureType()) {
				case AZURE: {
					data = getSignatureAzure(data);
					break;
				}
				case FILESYSTEM: {
					data = getSignatureFile(data);
					break;
				}
			}
		} catch (IOException | GeneralSecurityException ex) {
			log.error("Couldn't create File Signature: ", ex);
		}
		return data;
	}

	private byte[] getSignatureAzure(byte[] data) throws GeneralSecurityException, IOException {
		SecretClient secretClient = new SecretClientBuilder()
				.vaultUrl("https://" + signatureProperties.getAzureKeyVaultName() + ".vault.azure.net/")
        		.credential(new DefaultAzureCredentialBuilder().build())
				.buildClient();
		String privateKeyValue = secretClient.getSecret(signatureProperties.getAzureSecretName()).getValue();
		return getSignatureInputStream(new ByteArrayInputStream(privateKeyValue.getBytes()), data);
	}

	private byte[] getSignatureFile(byte[] data) throws GeneralSecurityException, IOException {
		return getSignatureInputStream(new ClassPathResource(signatureProperties.getFilePrivateKeyLocation()).getInputStream(), data);
	}

	private byte[] getSignatureInputStream(InputStream is, byte[] data) throws GeneralSecurityException, IOException {
		Signature ecdsa = Signature.getInstance(signatureProperties.getSignatureAlgorithm());
		ecdsa.initSign(getSignatureFilePrivateKey(is));
		ecdsa.update(data);
		return ecdsa.sign();
	}

	private PrivateKey getSignatureFilePrivateKey(InputStream inputStream) throws IOException,
																					GeneralSecurityException {
		return PemReader.loadPrivateKey(FileCopyUtils.copyToByteArray(inputStream), signatureProperties.getSignatureKeyType());
	}
}
