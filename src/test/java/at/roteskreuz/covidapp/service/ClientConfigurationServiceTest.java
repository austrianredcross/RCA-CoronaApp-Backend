package at.roteskreuz.covidapp.service;

import at.roteskreuz.covidapp.domain.ClientConfiguration;
import at.roteskreuz.covidapp.repository.ClientConfigurationRepository;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/*
 * @author Zoltán Puskai
 */
@SpringBootTest
public class ClientConfigurationServiceTest {

	@Autowired
	private ClientConfigurationService service;
	@MockBean
	private ClientConfigurationRepository repository;

	@BeforeEach
	public void setUp() {
		ClientConfiguration config = new ClientConfiguration();
		config.setData("DATA");
		config.setDateCreated(LocalDateTime.of(2020, Month.JANUARY, 1, 12, 0));
		config.setId(1L);

		Mockito.when(repository.findById(1L)).thenReturn(Optional.of(config));
	}

	@Test
	public void whenCalledWithId1_thenConfigShouldBeFound() {
		LocalDateTime lastModified = LocalDateTime.of(2020, Month.JANUARY, 1, 12, 0);
		String data = "DATA";

		ClientConfiguration found = service.getConfiuration();

		assertThat(found.getData()).isEqualTo(data);
		assertThat(found.getDateCreated()).isEqualTo(lastModified);
		assertThat(found.getId()).isEqualTo(1L);
	}

}
