package at.roteskreuz.covidapp.service;

import at.roteskreuz.covidapp.domain.ExportBatch;
import at.roteskreuz.covidapp.domain.ExportConfig;
import at.roteskreuz.covidapp.domain.Exposure;
import at.roteskreuz.covidapp.domain.SignatureInfo;
import at.roteskreuz.covidapp.model.ExportBatchStatus;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author zolika
 */
@SpringBootTest
public class FileServiceTest {

	@Autowired
	private FileService fileService;

	@Test
	public void testCreateFile() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, InvalidKeyException, SignatureException, IOException, InvalidKeySpecException {

		LocalDateTime now = LocalDateTime.now();

		String bucketName = "bucketName";
		String filenameRoot = "out";
		String region = "US";

		List<SignatureInfo> signatureInfos = Arrays.asList(new SignatureInfo(null, null, null, null, "1", "some_id", now.plusYears(1)));
		ExportConfig config = new ExportConfig(1L, bucketName, filenameRoot, Duration.ofSeconds(100L), region, now.minusDays(1), now, signatureInfos);

		ExportBatch batch = new ExportBatch(1L, config, bucketName, filenameRoot, now.minusDays(1), now, region, ExportBatchStatus.EXPORT_BATCH_OPEN, now.plusDays(1), signatureInfos);

		Exposure exposure1 = new Exposure("Fy/EgOWYSQw0F3YEM5sVQw==", 8, null, Arrays.asList(region), 2649980, 1, now, Boolean.FALSE, 1L,"red-warning");
		Exposure exposure2 = new Exposure("K2MFOsSDI43hdhaehh89zQ==", 1, null, Arrays.asList(region), 2649866, 114, now, Boolean.FALSE, 1L,"green-warning");

		//List<Exposure> exposures = Arrays.asList(exposure1, exposure2, exposure3, exposure4);
		List<Exposure> exposures = Arrays.asList(exposure1, exposure2);
		int batchNum = 1;
		int batchSize = 1;

		fileService.createFile(batch, exposures, batchNum, batchSize, signatureInfos);
	}

}
