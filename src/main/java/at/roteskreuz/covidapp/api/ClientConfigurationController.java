package at.roteskreuz.covidapp.api;

import at.roteskreuz.covidapp.domain.ClientConfiguration;
import at.roteskreuz.covidapp.service.ClientConfigurationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import java.time.ZoneOffset;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


/**
 * Controller for exposing client configuration via {@code /api/v?/configuration}.
 *
 * @author Zoltán Puskai
 * @since 0.0.1-SNAPSHOT
 */


@RestController
@RequestMapping(path = "/api/v${application.api.version}")
@RequiredArgsConstructor
@Api(tags = "Configuration", description = "Endpoint that returns the configuration for clients.")
public class ClientConfigurationController {

	private final ClientConfigurationService service;

	
	/**
	 * Returns the current client configuration ({@link ClientConfiguration})
	 *
	 * @return client configuration
	 */
	@GetMapping(value = "/configuration", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Returns the current configuration", authorizations = {
		@Authorization(value = "AuthorizationKey")})
	public ResponseEntity<String> configuration() {
		ClientConfiguration configuration = service.getConfiuration();
		if (configuration == null) {
			throw new ResponseStatusException(
			  HttpStatus.NOT_FOUND, "Client configuration not found"
			);
		}
		long lastModifiedDate = configuration.getDateCreated().toInstant(ZoneOffset.UTC).toEpochMilli();

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setDate("Last-Modified", lastModifiedDate);
		return ResponseEntity.ok()
				.headers(responseHeaders)
				.body(configuration.getData());
	}

}
