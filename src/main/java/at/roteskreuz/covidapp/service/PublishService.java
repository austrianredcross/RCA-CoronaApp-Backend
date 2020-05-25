package at.roteskreuz.covidapp.service;

import at.roteskreuz.covidapp.domain.Exposure;
import at.roteskreuz.covidapp.model.ApiResponse;
import at.roteskreuz.covidapp.model.Publish;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author zolika
 */
@Service
@RequiredArgsConstructor
public class PublishService {
	
	private final ExposureService exposureService;

	public ApiResponse publish(Publish publish)  {

		LocalDateTime now = LocalDateTime.now();
		
		publish.getRegions().replaceAll(String::toUpperCase);
		String regions = "," + String.join(", ", publish.getRegions()) + ",";
		
		exposureService.saveAll(
			publish.getKeys()
				.stream()
				.map(k-> 
					new Exposure( 
							k.getBinKey(), 
							k.getTransmissionRisk(), 
							publish.getAppPackageName(),
							regions, 
							k.getIntervalNumber(), 
							k.getIntervalCount(), 
							now , 
							true, 
							null,
							publish.getDiagnosisType()
					))
				.collect(Collectors.toList()));

		return ApiResponse.ok();
	}

}
