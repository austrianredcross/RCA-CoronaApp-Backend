package at.roteskreuz.covidapp.api;


import at.roteskreuz.covidapp.exception.AuthorizedAppNotFoundException;
import at.roteskreuz.covidapp.exception.DeviceCheckFailedException;
import at.roteskreuz.covidapp.exception.PlatformNotSupportedException;
import at.roteskreuz.covidapp.exception.RegionNotAllowedException;
import at.roteskreuz.covidapp.exception.SafetynetAttestationFailedException;
import at.roteskreuz.covidapp.model.ApiResponse;
import at.roteskreuz.covidapp.model.Publish;
import at.roteskreuz.covidapp.service.PublishService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller for storing published exposure keys
 *
 * @author Zoltán Puskai
 * @since 0.0.1-SNAPSHOT
 */


@RestController
@RequestMapping(path = "/api/v${application.api.version}")
@RequiredArgsConstructor
public class PublishController {

	private final PublishService publishService;

	@PostMapping(value = "/publish", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse> publish(@Valid @RequestBody Publish publish) throws AuthorizedAppNotFoundException, RegionNotAllowedException, PlatformNotSupportedException, DeviceCheckFailedException, SafetynetAttestationFailedException {
		ApiResponse response = publishService.publish(publish);
		return ResponseEntity.status(response.getStatus()).body(response);
	}

}
