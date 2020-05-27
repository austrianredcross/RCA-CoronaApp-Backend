package at.roteskreuz.covidapp.service;

import at.roteskreuz.covidapp.domain.ExportBatch;
import at.roteskreuz.covidapp.domain.ExportConfig;
import at.roteskreuz.covidapp.domain.Exposure;
import at.roteskreuz.covidapp.domain.SignatureInfo;
import at.roteskreuz.covidapp.model.ExportBatchStatus;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author zolika
 */
@SpringBootTest
public class FileServiceTest {

	@Autowired
	private WorkerService workerService;

	//@Test
	public void testCreateFile() throws Exception {

		LocalDateTime now = LocalDateTime.now();

		String bucketName = "exposures";
		String filenameRoot = "test";
		String region = "AT";

		List<SignatureInfo> signatureInfos = Arrays.asList(new SignatureInfo(null, null, null, null, "1", "some_id", now.plusYears(1)));
		ExportConfig config = new ExportConfig(1L, bucketName, filenameRoot, Duration.ofSeconds(100L), region, now.minusDays(1), now, signatureInfos);

		ExportBatch batch = new ExportBatch(1L, config, bucketName, filenameRoot, now.minusDays(1), now, region, ExportBatchStatus.EXPORT_BATCH_OPEN, now.plusDays(1), signatureInfos);

		Exposure exposure1 = new Exposure("Fy/EgOWYSQw0F3YEM5sVQw==", 8, null, region, 2649980, 1, now, Boolean.FALSE, 1L, "red-warning");
		Exposure exposure2 = new Exposure("K2MFOsSDI43hdhaehh89zQ==", 1, null, region, 2649866, 114, now, Boolean.FALSE, 1L, "green-warning");

		//List<Exposure> exposures = Arrays.asList(exposure1, exposure2, exposure3, exposure4);
		List<Exposure> exposures = Arrays.asList(exposure1, exposure2);
		int batchNum = 1;
		int batchSize = 1;

		//workerService.createFile(batch, exposures, batchNum, batchSize, signatureInfos);
	}

	private static LocalDateTime truncateToDuration(LocalDateTime zonedDateTime, Duration duration) {
		LocalDateTime startOfDay = zonedDateTime.truncatedTo(ChronoUnit.DAYS);
		return startOfDay.plus(duration.multipliedBy(
				Duration.between(startOfDay, zonedDateTime).dividedBy(duration.getSeconds()).getSeconds()));
	}

	//@Test
	public void test() {
		Duration oneMinute = Duration.of(1, ChronoUnit.MINUTES);
		Duration fiveMinutes = Duration.of(5, ChronoUnit.MINUTES);
		Duration fifteenMinutes = Duration.of(15, ChronoUnit.MINUTES);
		Duration oneHour = Duration.of(1, ChronoUnit.HOURS);
		Duration sixHours = Duration.of(6, ChronoUnit.HOURS);
		Duration oneDay = Duration.of(1, ChronoUnit.DAYS);

		LocalDateTime now = LocalDateTime.parse("2018-02-24T10:37:07.123");

		assertEquals(LocalDateTime.parse("2018-02-24T10:37:00.000"),
				truncateToDuration(now, oneMinute));
		assertEquals(LocalDateTime.parse("2018-02-24T10:35:00.000"),
				truncateToDuration(now, fiveMinutes));
		assertEquals(LocalDateTime.parse("2018-02-24T10:30:00.000"),
				truncateToDuration(now, fifteenMinutes));
		assertEquals(LocalDateTime.parse("2018-02-24T10:00:00.000"),
				truncateToDuration(now, oneHour));
		assertEquals(LocalDateTime.parse("2018-02-24T06:00:00.000"),
				truncateToDuration(now, sixHours));
		assertEquals(LocalDateTime.parse("2018-02-24T00:00:00.000"),
				truncateToDuration(now, oneDay));
	}	
	
}
