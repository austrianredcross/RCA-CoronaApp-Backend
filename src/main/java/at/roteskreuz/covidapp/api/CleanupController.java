package at.roteskreuz.covidapp.api;


import at.roteskreuz.covidapp.model.ApiResponse;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
@Api(tags = "Cleanup", description = "Endpoint for cleaning up old data.")
public class CleanupController {


	@GetMapping(value = "/cleanup-export", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse> cleanupExport()  {
		return ResponseEntity.ok().body(ApiResponse.ok());
	}

	@GetMapping(value = "/cleanup-exposure", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse> cleanupExposure()  {
		return ResponseEntity.ok().body(ApiResponse.ok());
	}


}
