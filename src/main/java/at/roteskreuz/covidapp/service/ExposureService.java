package at.roteskreuz.covidapp.service;

import at.roteskreuz.covidapp.config.ApplicationConfig;
import at.roteskreuz.covidapp.domain.Exposure;
import at.roteskreuz.covidapp.repository.ExposureRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
	 *
	 * @param exposure exposure to be saved
	 */
	public void save(Exposure exposure) {
		Exposure existingExposure = exposureRepository.findById(exposure.getExposureKey()).orElse(null);
		if (existingExposure != null) {
			if (existingExposure.getPassword() != null
					&& existingExposure.getPassword().equals(exposure.getPassword())
					&& existingExposure.getIntervalNumber().equals(exposure.getIntervalNumber())
					&& existingExposure.getIntervalCount().equals(exposure.getIntervalCount())) {
				//checking if update should be made
				existingExposure.setUpdatedAt(LocalDateTime.now());
				existingExposure.setDiagnosisType(exposure.getDiagnosisType());
				exposureRepository.save(existingExposure);
			} else {
				//fail silently
				log.error(String.format("SILENT_FAIL - Exposure with key: %s is not valid", exposure.getExposureKey()));
			}
		} else {
			exposureRepository.save(exposure);
		}
	}

	/**
	 * Finds exposures belonging wit red warnings
	 *
	 * @param start start timestamp
	 * @param end end timestamp
	 * @param diagnosisType diagnosis type
	 * @param region region
	 * @return
	 */
	public List<Exposure> findExposuresForExport(LocalDateTime start, LocalDateTime end, String diagnosisType, String region) {

		int since = (int) (start.toInstant(ZoneOffset.UTC).getEpochSecond() / ApplicationConfig.INTERVAL_LENGTH.getSeconds());
		int until = (int) (end.toInstant(ZoneOffset.UTC).getEpochSecond() / ApplicationConfig.INTERVAL_LENGTH.getSeconds());
		return exposureRepository.findByIntervalNumberGreaterThanEqualAndIntervalNumberLessThanAndDiagnosisTypeAndRegionsLike(since, until, diagnosisType, "%," + region + ",%");
	}

	/**
	 * Deletes exposures that are older than interval number for a region
	 *
	 * @param intervalNumber interval number
	 * @param region region
	 * @return
	 */
	public List<Exposure> cleanUpExposures(int intervalNumber, String region) {

		return exposureRepository.deleteAllByIntervalNumberIsLessThanAndRegionsLike(intervalNumber, "%," + region + ",%");
	}
}
