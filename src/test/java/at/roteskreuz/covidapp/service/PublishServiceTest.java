package at.roteskreuz.covidapp.service;

import at.roteskreuz.covidapp.model.Publish;
import at.roteskreuz.covidapp.util.PublishUtil;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/*
 * @author Zoltán Puskai
 */
@SpringBootTest
public class PublishServiceTest {

	@Autowired
	private PublishService publishService;
	@MockBean
	private  ExposureService exposureService;

	@Test
	public void whenPublishEachExposureShouldBeSaved() {
		Mockito.doNothing().when(exposureService).save(Mockito.any());
		Random random = new Random();
		int exposuresCount = random.nextInt(10);
		Publish publish = PublishUtil.createPublish(exposuresCount);		
		publishService.publish(publish);
		Mockito.verify(exposureService, Mockito.times(exposuresCount)).save(Mockito.any());
	}
	
}
