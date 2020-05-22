package at.roteskreuz.covidapp.service;

import at.roteskreuz.covidapp.domain.Exposure;
import at.roteskreuz.covidapp.exception.AuthorizedAppNotFoundException;
import at.roteskreuz.covidapp.repository.ExposureRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author zolika
 */

@Service
@RequiredArgsConstructor
public class ExposureService {
	
	
	private final ExposureRepository exposureRepository;

	public void saveAll(List<Exposure> exposures) throws AuthorizedAppNotFoundException {
		exposureRepository.saveAll(exposures);
	}
}
