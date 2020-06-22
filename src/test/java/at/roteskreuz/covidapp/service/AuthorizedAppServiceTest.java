package at.roteskreuz.covidapp.service;

import at.roteskreuz.covidapp.domain.AuthorizedApp;
import at.roteskreuz.covidapp.repository.AuthorizedAppRepository;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/*
 * @author Zoltán Puskai
 */
@SpringBootTest
public class AuthorizedAppServiceTest {

	@Autowired
	private AuthorizedAppService service;
	@MockBean
	private  AuthorizedAppRepository repository;


	@Test
	public void whenCalledWithAnId_thenAuthorizedAppShouldBeFound() {
		String id = "at.roteskreuz.stopcorona.stage";
		String platform = "android";

		AuthorizedApp app = new AuthorizedApp();
		app.setAppPackageName(id);
		app.setPlatform(platform);
		Mockito.when(repository.findById(id)).thenReturn(Optional.of(app));

		AuthorizedApp found = service.findById(id);

		assertThat(found.getAppPackageName()).isEqualTo(id);
		assertThat(found.getPlatform()).isEqualTo(platform);
	}

}
