package at.roteskreuz.covidapp.api;

import at.roteskreuz.covidapp.config.ApplicationConfig;
import at.roteskreuz.covidapp.domain.AuthorizedApp;
import at.roteskreuz.covidapp.model.ApiResponse;
import at.roteskreuz.covidapp.model.ExposureKey;
import at.roteskreuz.covidapp.model.Publish;
import at.roteskreuz.covidapp.model.VerificationPayload;
import at.roteskreuz.covidapp.properties.PublishProperties;
import at.roteskreuz.covidapp.service.AuthorizedAppService;
import at.roteskreuz.covidapp.service.DeviceCheckService;
import at.roteskreuz.covidapp.service.PublishService;
import at.roteskreuz.covidapp.service.SafetynetAttestationService;
import at.roteskreuz.covidapp.service.TanService;
import at.roteskreuz.covidapp.validation.PublishValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
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
	@MockBean
	private DeviceCheckService deviceCheckService;
	@MockBean
	private SafetynetAttestationService safetynetAttestationService;

	@Autowired
	private ObjectMapper objectMapper;
	private Publish publish;

	private final String uuid = "dbea77de-bad5-46bb-9724-fd0f8ae13c33";
	private final String authorization = "123456";
	private final String diagnosisType = "red-warning";

	@BeforeEach
	public void setUp() {

		publish = new Publish();
		publish.setAppPackageName("at.roteskreuz.stopcorona.stage");
		publish.setPlatform("android");
		publish.setDiagnosisType(diagnosisType);
		publish.setRegions(Arrays.asList("AT"));
		ExposureKey exposureKey = new ExposureKey();
		exposureKey.setIntervalCount(1);

		LocalDateTime now = LocalDateTime.now().minusMinutes(15);
		long intervalNumber = now.toInstant(ZoneOffset.UTC).getEpochSecond() / ApplicationConfig.INTERVAL_LENGTH.getSeconds();

		exposureKey.setIntervalNumber((int) intervalNumber);
		exposureKey.setKey("MTIzNDU2Nzg5MDEyMzQ1Ng==");
		exposureKey.setPassword("password");
		publish.setKeys(Arrays.asList(exposureKey));
		publish.setVerificationAuthorityName("RedCross");
		VerificationPayload verificationPayload = new VerificationPayload();
		verificationPayload.setAuthorization(authorization);
		verificationPayload.setUuid(uuid);
		publish.setVerificationPayload(verificationPayload);
		Mockito.when(publishValidator.isValid(Mockito.any(Publish.class), Mockito.any(ConstraintValidatorContext.class))).thenReturn(Boolean.TRUE);
		AuthorizedApp authorizedApp = new AuthorizedApp();
		authorizedApp.setAllowedRegions(publish.getRegions());
		authorizedApp.setAppPackageName(publish.getAppPackageName());
		Mockito.when(authorizedAppService.findById(publish.getAppPackageName())).thenReturn(authorizedApp);
		Mockito.when(deviceCheckService.isDeviceTokenValid(Mockito.any())).thenReturn(Boolean.TRUE);
		Mockito.when(safetynetAttestationService.isAttestationValid(Mockito.any())).thenReturn(Boolean.TRUE);
		Mockito.when(publishService.publish(Mockito.any())).thenReturn(ApiResponse.ok());
	}

	@Test
	public void validPublishRequestShouldReturnOk() throws Exception {
		Mockito.when(tanService.validate(uuid, authorization, diagnosisType)).thenReturn(Boolean.TRUE);
		this.mockMvc.perform(post("/api/v" + appVersion + "/publish").content(objectMapper.writeValueAsString(publish)).contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	public void invalidPublishRequestShouldBeForbidden() throws Exception {
		Mockito.when(tanService.validate(uuid, authorization, diagnosisType)).thenReturn(Boolean.FALSE);
		this.mockMvc.perform(post("/api/v" + appVersion + "/publish").content(objectMapper.writeValueAsString(publish)).contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isForbidden());
	}
	
	
}
