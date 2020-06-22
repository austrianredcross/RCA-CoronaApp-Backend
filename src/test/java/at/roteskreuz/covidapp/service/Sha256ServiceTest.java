package at.roteskreuz.covidapp.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/*
 * @author Zoltán Puskai
 */
@SpringBootTest
public class Sha256ServiceTest {

	@Autowired
	private Sha256Service service;

	@Test
	public void sha256Value() {
		Assertions.assertThat(service.sha256("alma a fa alatt").equals("b5771e55a29bbd9010eee0baf466301831ebe798f01c41950c2ef15cad88b132"));
	}

	@Test
	public void sha256BytesValue() {
		Assertions.assertThat(service.sha256("alma a fa alatt".getBytes()).equals("b5771e55a29bbd9010eee0baf466301831ebe798f01c41950c2ef15cad88b132"));
	}

	
}
