package at.roteskreuz.covidapp.service;

import at.roteskreuz.covidapp.blobstore.Blobstore;
import at.roteskreuz.covidapp.domain.ExportBatch;
import at.roteskreuz.covidapp.domain.ExportFile;
import at.roteskreuz.covidapp.domain.Exposure;
import at.roteskreuz.covidapp.model.ApiResponse;
import at.roteskreuz.covidapp.model.ExportBatchStatus;
import at.roteskreuz.covidapp.repository.ExportBatchRepository;
import at.roteskreuz.covidapp.repository.ExportFileRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author zolika
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CleanupService {

	//private static final Duration MIN_CLEANUP_TTL = Duration.ofDays(10);

	private static final Duration MIN_CLEANUP_TTL = Duration.ofMinutes(1);
	
	@Value("${cleanup.ttl:PT1M}")
	private Duration cleanupTtl;

	private final ExposureService exposureService;
	private final ExportBatchRepository exportBatchRepository;
	private final ExportFileRepository exportFileRepository;
	private final Blobstore blobstore;

	public ApiResponse cleanupExport() {
		List<ExportBatch> delExportBatches = exportBatchRepository.findByEndTimestampBeforeAndStatusIsNot(getCutOffDate(), ExportBatchStatus.EXPORT_BATCH_DELETED);
		if(delExportBatches != null) {
			delExportBatches.forEach((exportBatch) -> {
				boolean allCurBatchDeleted = true;
				List<ExportFile> exportFiles = exportFileRepository.findAllByBatchIsAndStatusIsNot(exportBatch, ExportBatchStatus.EXPORT_BATCH_DELETED);
				if(exportFiles != null) {
					for (ExportFile exportFile : exportFiles) {
						try {
							if (!blobstore.deleteObject(exportFile.getBucketName(), exportFile.getFilename())) {
								allCurBatchDeleted = false;
								log.error("Couldn't delete exportFile: " + exportFile.toString());
							} else {
								exportFile.setStatus(ExportBatchStatus.EXPORT_BATCH_DELETED);
								exportFileRepository.save(exportFile);
								log.info("Deleted exportFile: " + exportFile.toString());
							}
						} catch (Exception ex) {
							log.error("Couldn't delete exportFile: " + exportFile.toString(), ex);
						}
					}
				}
				if (allCurBatchDeleted) {
					exportBatch.setStatus(ExportBatchStatus.EXPORT_BATCH_DELETED);
					exportBatchRepository.save(exportBatch);
				}
			});
		}
		return ApiResponse.ok();
	}

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
