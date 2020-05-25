package at.roteskreuz.covidapp.api;


import at.roteskreuz.covidapp.model.ApiResponse;
import at.roteskreuz.covidapp.service.CleanupService;
import at.roteskreuz.covidapp.service.WorkerService;
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

	private final CleanupService cleanupService;

	@GetMapping(value = "/cleanup-export", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse> cleanupExport()  {
		ApiResponse response = cleanupService.cleanupExport();
		return ResponseEntity.status(response.getStatus()).body(response);
	}

	@GetMapping(value = "/cleanup-exposure", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse> cleanupExposure()  {
		ApiResponse response = cleanupService.cleanupExposure();
		return ResponseEntity.status(response.getStatus()).body(response);
	}


}
