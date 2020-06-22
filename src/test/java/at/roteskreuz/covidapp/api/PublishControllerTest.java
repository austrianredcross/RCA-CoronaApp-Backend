package at.roteskreuz.covidapp.api;

import at.roteskreuz.covidapp.domain.AuthorizedApp;
import at.roteskreuz.covidapp.model.ApiResponse;
import at.roteskreuz.covidapp.model.Publish;
import at.roteskreuz.covidapp.properties.PublishProperties;
import at.roteskreuz.covidapp.service.AuthorizedAppService;
import at.roteskreuz.covidapp.service.PublishService;
import at.roteskreuz.covidapp.service.TanService;
import at.roteskreuz.covidapp.util.PublishUtil;
import at.roteskreuz.covidapp.validation.PublishValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * @author Zoltán Puskai
 */
@WebMvcTest(PublishController.class)
public class PublishControllerTest {

	@Value("${application.api.version}")
	private String appVersion;
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private PublishService publishService;
	@MockBean
	private TanService tanService;
	@Autowired
	private PublishProperties publishProperties;
	@MockBean
	private PublishValidator publishValidator;
	@MockBean
	private AuthorizedAppService authorizedAppService;

	@Autowired
	private ObjectMapper objectMapper;
	private Publish publish;


	@BeforeEach
	public void setUp() {

		publish = PublishUtil.createPublish(1);

		Mockito.when(publishValidator.isValid(Mockito.any(Publish.class), Mockito.any(ConstraintValidatorContext.class))).thenReturn(Boolean.TRUE);
		AuthorizedApp authorizedApp = new AuthorizedApp();
		authorizedApp.setAllowedRegions(publish.getRegions());
		authorizedApp.setAppPackageName(publish.getAppPackageName());
		Mockito.when(authorizedAppService.findById(publish.getAppPackageName())).thenReturn(authorizedApp);
		Mockito.when(publishService.publish(Mockito.any())).thenReturn(ApiResponse.ok());
	}

	@Test
	public void validPublishRequestShouldReturnOk() throws Exception {
		Mockito.when(tanService.validate(publish.getVerificationPayload().getUuid(), publish.getVerificationPayload().getAuthorization(), publish.getDiagnosisType())).thenReturn(Boolean.TRUE);
		this.mockMvc.perform(post("/api/v" + appVersion + "/publish").content(objectMapper.writeValueAsString(publish)).contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	public void invalidPublishRequestShouldBeForbidden() throws Exception {
		Mockito.when(tanService.validate(publish.getVerificationPayload().getUuid(), publish.getVerificationPayload().getAuthorization(), publish.getDiagnosisType())).thenReturn(Boolean.FALSE);
 		this.mockMvc.perform(post("/api/v" + appVersion + "/publish").content(objectMapper.writeValueAsString(publish)).contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isForbidden());
	}
	
	
}
