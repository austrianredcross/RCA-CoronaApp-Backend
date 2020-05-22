package at.roteskreuz.covidapp.api;


import at.roteskreuz.covidapp.model.ApiResponse;
import at.roteskreuz.covidapp.service.BatchService;
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
public class ExportController {
	
	private final BatchService batchService;

	@GetMapping(value = "/export/create-batches", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse> createBatches()  {
		ApiResponse response = batchService.createBratches();
		return ResponseEntity.status(response.getStatus()).body(response);
	}

	@GetMapping(value = "/export/do-work", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse> doWork()  {
		ApiResponse response = batchService.doWork();
		return ResponseEntity.status(response.getStatus()).body(response);
	}


}
