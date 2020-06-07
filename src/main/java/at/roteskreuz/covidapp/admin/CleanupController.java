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
	 * Cleans up old exposures and old export files
	 * @return API response
	 * @throws java.lang.Exception
	 */
	@GetMapping(value = "/cleanup", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse> cleanupExport() throws Exception  {
		ApiResponse response = cleanupService.cleanup();
		return ResponseEntity.status(response.getStatus()).body(response);
	}

}
