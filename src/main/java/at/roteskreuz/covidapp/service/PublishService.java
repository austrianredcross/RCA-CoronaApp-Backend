package at.roteskreuz.covidapp.service;

import at.roteskreuz.covidapp.domain.Exposure;
import at.roteskreuz.covidapp.model.ApiResponse;
import at.roteskreuz.covidapp.model.Publish;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service class that processes publish requests
 * 
 * @author Zoltán Puskai
 */
@Service
@RequiredArgsConstructor
public class PublishService {

	private final ExposureService exposureService;

	/**
	 * Processes publish requests.
	 * Saves the exposures from the publish request
	 * 
	 * @param publish publish request
	 * @return 
	 */
	public ApiResponse publish(Publish publish) {

		LocalDateTime now = LocalDateTime.now();

		publish.getRegions().replaceAll(String::toUpperCase);
		String regions = "," + String.join(", ", publish.getRegions()) + ",";

		publish.getKeys().forEach(k -> {
			exposureService.save(
				new Exposure(
					k.binKey(),
					k.getPassword(),
					publish.getAppPackageName(),
					regions,
					k.getIntervalNumber(),
					k.getIntervalCount(),
					now,
					true,
					null,
					publish.getDiagnosisType(),
					null
				)
			);
		});

		return ApiResponse.ok();
	}

}
