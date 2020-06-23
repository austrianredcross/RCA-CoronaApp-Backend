package at.roteskreuz.covidapp.service;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/*
 * @author Zoltán Puskai
 */
@SpringBootTest
public class TanServiceTest {

	@Autowired
	private TanService service;

	@MockBean
	private RestTemplate restTemplate;
	
	@Value("${external.personal.data.storage.url:}")
	private String personalDataServiceUrl;	

	@Test
	public void whenCalledWithValidTanItShouldReturnTrue() {
		String tan = "975310";
		String uuid= "1d63a871-d530-4b72-93e5-7b4a2ce4bd2f";
		String type= "red-warning";
		
		ResponseEntity<String> response200 = new ResponseEntity("OK",HttpStatus.OK);		
		Mockito.when(restTemplate.postForEntity(Mockito.eq(personalDataServiceUrl), Mockito.any(HttpEntity.class), Mockito.eq(String.class))).thenReturn(response200);
		assertThat(service.validate(uuid, tan, type)).isEqualTo(true);
	}
	
	@Test
	public void whenCalledWithInvalidTanItShouldReturnFalse() {
		String tan = "975";
		String uuid= "1d63a871-d530-4b72-93e5-7b4a2ce4bd2f";
		String type= "red-warning";		
		assertThat(service.validate(uuid, tan, type)).isEqualTo(false);
	}

	@Test
	public void whenTanServiceIsNotAvailableItShouldReturnTrue() {
		String tan = "975310";
		String uuid= "1d63a871-d530-4b72-93e5-7b4a2ce4bd2f";
		String type= "red-warning";
		
		ResponseEntity<String> response404 = new ResponseEntity("OK",HttpStatus.NOT_FOUND);		
		Mockito.when(restTemplate.postForEntity(Mockito.eq(personalDataServiceUrl), Mockito.any(HttpEntity.class), Mockito.eq(String.class))).thenReturn(response404);
		assertThat(service.validate(uuid, tan, type)).isEqualTo(false);
	}	

}
