package at.roteskreuz.covidapp.service;

import at.roteskreuz.covidapp.domain.Exposure;
import at.roteskreuz.covidapp.repository.ExposureRepository;
import at.roteskreuz.covidapp.util.ExposureUtil;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/*
 * @author Zoltán Puskai
 */
@SpringBootTest
public class ExposureServiceTest {

	@Autowired
	private ExposureService service;
	@MockBean
	private ExposureRepository repository;

	/*
	public void save(Exposure exposure) {
	public List<Exposure> findExposuresForExport(LocalDateTime start, LocalDateTime end, String diagnosisType, String region) {
	public List<Exposure> cleanUpExposures(int intervalNumber, String region) {
	 */
	@Test
	public void saveShouldCallRepositorySave() {
		Exposure exposure = ExposureUtil.createExposures(1).get(0);
		Mockito.when(repository.save(exposure)).thenReturn(exposure);
		service.save(exposure);
		Mockito.verify(repository, Mockito.times(1)).save(exposure);
	}
	
	@Test
	public void  findExposuresForExportShouldSearchThroughRepository() {
		Random random = new Random();
		int exposuresCount = random.nextInt(3);
		List<Exposure> exposures = ExposureUtil.createExposures(exposuresCount);
		Mockito.when(repository.findByIntervalNumberGreaterThanEqualAndIntervalNumberLessThanAndDiagnosisTypeAndRegionsLike(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(exposures);
		List<Exposure> exposuresFound  = service.findExposuresForExport(LocalDateTime.now().minusDays(1), LocalDateTime.now(), "red-warning", "AT");
		Assertions.assertThat(exposures == exposuresFound);
		Mockito.verify(repository, Mockito.times(1)).findByIntervalNumberGreaterThanEqualAndIntervalNumberLessThanAndDiagnosisTypeAndRegionsLike(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
	}
	
	@Test
	public void cleanupShouldDeleteThroughRepository() {
		Random random = new Random();
		int intervalNumber = random.nextInt(20);
		String region = "AT";
		String regionQuery = "%," + region + ",%";
		Mockito.when(repository.deleteAllByIntervalNumberIsLessThanAndRegionsLike(intervalNumber, regionQuery)).thenReturn(Collections.EMPTY_LIST);
		service.cleanUpExposures(intervalNumber, region);
		Mockito.verify(repository, Mockito.times(1)).deleteAllByIntervalNumberIsLessThanAndRegionsLike(intervalNumber, regionQuery);
	}
}
