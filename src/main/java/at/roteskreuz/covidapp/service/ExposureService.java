package at.roteskreuz.covidapp.service;

import at.roteskreuz.covidapp.domain.ExportBatch;
import at.roteskreuz.covidapp.domain.Exposure;
import at.roteskreuz.covidapp.domain.ExposureCriteria;
import at.roteskreuz.covidapp.domain.ExposureSpecificationsBuilder;
import at.roteskreuz.covidapp.repository.ExposureRepository;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author zolika
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class ExposureService {

	private final ExposureRepository exposureRepository;

	public void saveAll(List<Exposure> exposures) {
		exposureRepository.saveAll(exposures);
	}

	public List<Exposure> findExposuresForBatch(ExportBatch batch) {
 		ExposureCriteria criteria  = new ExposureCriteria(batch.getRegion(), batch.getStartTimestamp(), batch.getEndTimestamp(), false);
		ExposureSpecificationsBuilder builder = new ExposureSpecificationsBuilder(criteria);
		return exposureRepository.findAll(builder.build());
	}

	public List<Exposure> cleanUpExposures(LocalDateTime cleanupTtl){
		return exposureRepository.deleteAllByCreatedAtIsLessThan(cleanupTtl);
	}
}
