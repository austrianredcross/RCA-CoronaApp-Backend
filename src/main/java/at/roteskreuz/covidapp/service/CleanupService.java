package at.roteskreuz.covidapp.service;

import at.roteskreuz.covidapp.blobstore.Blobstore;
import at.roteskreuz.covidapp.domain.Exposure;
import at.roteskreuz.covidapp.model.ApiResponse;
import at.roteskreuz.covidapp.repository.ExportFileRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Cleans up old files and old exposures
 * Exported files are signed with private keys during the export process.
 * 
 * @author Bernhard Roessler
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CleanupService {

	private static final Duration MIN_CLEANUP_TTL = Duration.ofDays(10);

	@Value("${cleanup.ttl:P14D}")
	private Duration cleanupTtl;

	private final ExposureService exposureService;
	private final ExportFileRepository exportFileRepository;
	private final Blobstore blobstore;

	/**
	 * Cleans up old files older
	 * @return API response
	 */
	public ApiResponse cleanupExport() {

		return ApiResponse.ok();
	}
	/**
	 * Cleans up old exposures
	 * @return API response
	 */
	public ApiResponse cleanupExposure() {
		List<Exposure> delExposures = exposureService.cleanUpExposures(getCutOffDate());
		log.info(String.format("%d Exposures deleted", delExposures == null ? 0 : delExposures.size()));
		return ApiResponse.ok();
	}

	private LocalDateTime getCutOffDate() {
		//Duration cleanupTtl = this.cleanupTtl;
		if (cleanupTtl.compareTo(MIN_CLEANUP_TTL) < 0) {
			cleanupTtl = MIN_CLEANUP_TTL;
		}
		return LocalDateTime.now().minus(cleanupTtl);
	}
}
