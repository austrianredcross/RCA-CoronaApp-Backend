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
 * Service class to manage exposures
 * 
 * @author Zoltán Puskai
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExposureService {

	private final ExposureRepository exposureRepository;

	/**
	 * Saves an exposure
	 * @param exposure exposure to be saved
	 */
	public void save(Exposure exposure) {
		Exposure existingExposure = exposureRepository.findById(exposure.getExposureKey()).orElse(null);
		if (existingExposure != null) {
			if (existingExposure.getPassword() != null && 
				existingExposure.getPassword().equals(exposure.getPassword()) && 
				existingExposure.getIntervalNumber().equals(exposure.getIntervalNumber()) &&
				existingExposure.getIntervalCount().equals(exposure.getIntervalCount())) {
				//checking if update should be made
				existingExposure.setUpdatedAt(LocalDateTime.now());
				existingExposure.setTransmissionRisk(exposure.getTransmissionRisk());
				existingExposure.setDiagnosisType(exposure.getDiagnosisType());
				exposureRepository.save(existingExposure);
			} else {
				//fail silently
				log.error(String.format("SILENT_FAIL - Exposure with key: %s is not valid" , exposure.getExposureKey()));
			}
		} else {
			exposureRepository.save(exposure);
		}
	}
	/**
	 * Finds exposures belonging to a batch
	 * @param batch batch for which the exposures belong
	 * @return 
	 */
	public List<Exposure> findExposuresForBatch(ExportBatch batch) {
 		ExposureCriteria criteria  = new ExposureCriteria(batch.getRegion(), batch.getStartTimestamp(), batch.getEndTimestamp(), false);
		ExposureSpecificationsBuilder builder = new ExposureSpecificationsBuilder(criteria);
		return exposureRepository.findAll(builder.build());
	}

	/**
	 * Deletes exposures that are older than cleanupTtl
	 * 
	 * @param cleanupTtl
	 * @return 
	 */
	public List<Exposure> cleanUpExposures(LocalDateTime cleanupTtl){
		return exposureRepository.deleteAllByCreatedAtIsLessThan(cleanupTtl);
	}
}
