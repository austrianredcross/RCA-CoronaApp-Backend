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
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author zolika
 */
//@SpringBootTest
public class FileServiceTest {

	@Autowired
	private FileService fileService;
	
	//@Test
	public void testCreateFile() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, InvalidKeyException, SignatureException, IOException, InvalidKeySpecException  {
		
		LocalDateTime now = LocalDateTime.now();
		
		String bucketName = "bucketName";
		String filenameRoot = "out";
		String region = "AT";
		
		List<SignatureInfo> signatureInfos = Arrays.asList(new SignatureInfo(null, null, null, null, "1", "some_id", now.plusYears(1)));
		ExportConfig config = new ExportConfig(1L, bucketName, filenameRoot, Duration.ofSeconds(100L), region, now.minusDays(1), now, signatureInfos);
		
		ExportBatch batch = new ExportBatch(1L, config, bucketName, filenameRoot, now.minusDays(1), now, region, ExportBatchStatus.EXPORT_BATCH_OPEN, now.plusDays(1), signatureInfos);
		
		Exposure exposure1 = new Exposure("QWxtYSBhIGZhIGFsYXR0Mg==", 1, bucketName, Arrays.asList(region), 2649825, 1, now, Boolean.FALSE, 1L);
		Exposure exposure2 = new Exposure("QWxtYSBhIGZhIGFsYXR0Mg==", 4, bucketName, Arrays.asList(region), 2649826, 1, now, Boolean.FALSE, 1L);
		Exposure exposure3 = new Exposure("QWxtYSBhIGZhIGFsYXR0Mg==", 4, bucketName, Arrays.asList(region), 2649827, 2, now, Boolean.FALSE, 1L);
		Exposure exposure4 = new Exposure("QWxtYSBhIGZhIGFsYXR0Mg==", 5, bucketName, Arrays.asList(region), 2649829, 1, now, Boolean.FALSE, 1L);

		//List<Exposure> exposures = Arrays.asList(exposure1, exposure2, exposure3, exposure4);
		List<Exposure> exposures = Arrays.asList();
		int batchNum = 11;
		int batchSize = 199;
		
	fileService.createFile(batch, exposures, batchNum, batchSize, signatureInfos);
	}
	
}
