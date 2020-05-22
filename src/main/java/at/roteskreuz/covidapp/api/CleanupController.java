package at.roteskreuz.covidapp.api;


import at.roteskreuz.covidapp.model.ApiResponse;
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
public class CleanupController {


	@GetMapping(value = "/cleanup-export", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse> cleanupExport()  {
		ApiResponse response = new ApiResponse(200, "OK", "", 1, false);
		return ResponseEntity.status(response.getStatus()).body(response);
	}

	@GetMapping(value = "/cleanup-exposure", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse> cleanupExposure()  {
		ApiResponse response = new ApiResponse(200, "OK", "", 1, false);
		return ResponseEntity.status(response.getStatus()).body(response);
	}


}
