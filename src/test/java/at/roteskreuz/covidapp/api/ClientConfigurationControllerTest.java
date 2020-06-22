package at.roteskreuz.covidapp.api;

import at.roteskreuz.covidapp.domain.ClientConfiguration;
import at.roteskreuz.covidapp.service.ClientConfigurationService;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.springframework.http.HttpHeaders.LAST_MODIFIED;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientConfigurationController.class)
public class ClientConfigurationControllerTest {
	
	@Value("${application.api.version}")
	private String appVersion;

	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ClientConfigurationService service;
	
	@Test
	public void configShouldReturnConfiguration() throws Exception {
		ClientConfiguration config = new ClientConfiguration();
		config.setData("DATA");
		config.setDateCreated(LocalDateTime.of(2020, Month.JANUARY, 1, 12, 0));
		config.setId(1L);		
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.US);
		String lastModifiedDate = dateFormat.format(Date.from(config.getDateCreated().atZone( ZoneId.systemDefault()).toInstant()));
		when(service.getConfiuration()).thenReturn(config);
		this.mockMvc.perform(get("/api/v" + appVersion + "/configuration"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString(config.getData())))
				.andExpect(header().string(LAST_MODIFIED,startsWith(lastModifiedDate)));
	}	

	@Test
	public void emptyConfigShouldReturnNotFound() throws Exception {	
		when(service.getConfiuration()).thenReturn(null);
		this.mockMvc.perform(get("/api/v" + appVersion + "/configuration"))
				.andDo(print())
				.andExpect(status().isNotFound());
	}
}
