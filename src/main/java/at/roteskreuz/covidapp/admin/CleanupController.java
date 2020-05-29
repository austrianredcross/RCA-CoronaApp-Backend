package at.roteskreuz.covidapp.admin;


import at.roteskreuz.covidapp.model.ApiResponse;
import at.roteskreuz.covidapp.service.CleanupService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for cleaning up old export files and old exposures
 *
 * @author Zoltán Puskai
 */
@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
@Api(tags = "Cleanup", description = "Endpoint for cleaning up old data.")
public class CleanupController {

	private final CleanupService cleanupService;

	/**
	 * Cleans up old export files
	 * @return Api response
	 */
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
