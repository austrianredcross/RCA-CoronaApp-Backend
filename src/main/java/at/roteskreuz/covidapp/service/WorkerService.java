package at.roteskreuz.covidapp.service;

import at.roteskreuz.covidapp.domain.ExportBatch;
import at.roteskreuz.covidapp.domain.ExportFile;
import at.roteskreuz.covidapp.domain.Exposure;
import at.roteskreuz.covidapp.domain.SignatureInfo;
import at.roteskreuz.covidapp.model.ApiResponse;
import at.roteskreuz.covidapp.model.ExportBatchStatus;
import at.roteskreuz.covidapp.properties.ExportProperties;
import at.roteskreuz.covidapp.repository.ExportFileRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
public class WorkerService {

	private final BatchService batchService;
	private final ExportProperties exportProperties;
	private final ExposureService exposureService;
	private final BlobstoreService blobstoreService;
	private final ExportFileRepository exportFileRepository;

	public ApiResponse doWork() throws Exception {
		boolean emitIndexForEmptyBatch = true;
		while (true) {

			// Only consider batches that closed a few minutes ago to allow the publish windows to close properly.
			LocalDateTime minutesAgo = LocalDateTime.now().minusMinutes(5);

			ExportBatch batch = batchService.leaseBatch(exportProperties.getWorkerTimeout(), minutesAgo);
			if (batch == null) {
				break;
			}
			exportBatch(batch, emitIndexForEmptyBatch);

			// We re-write the index file for empty batches for self-healing so that the index
			// file reflects the ExportFile table in database. However, if a single worker
			// processes a number of empty batches quickly, we want to avoid writing the same
			// file repeatedly and hitting a rate limit.
			emitIndexForEmptyBatch = false;
		}

		return ApiResponse.ok();
	}

	private void exportBatch(ExportBatch batch, boolean emitIndexForEmptyBatch) throws Exception {

		log.info(String.format("Processing export batch %s (root: %s, region: %s), max records per file %s", batch.getBatchId(), batch.getFilenameRoot(), batch.getRegion(), exportProperties.getMaxRecords()));

		List<Exposure> exposures = new ArrayList<>();
		List<List<Exposure>> groups = new ArrayList<>();

		// Build up groups of exposures in memory. We need to use memory so we can determine the
		// total number of groups (which is embedded in each export file). This technique avoids
		// SELECT COUNT which would lock the database slowing new uploads.	
		List<Exposure> exposuresForBatch = exposureService.findExposuresForBatch(batch);
		for (Exposure exposure : exposuresForBatch) {
			exposures.add(exposure);
			if (exposures.size() == exportProperties.getMaxRecords()) {
				groups.add( exposures);
				exposures = new ArrayList<>();
			}
		}
		// Create a group for any remaining keys.
		if (!exposures.isEmpty()) {
			groups.add(exposures);
		}
		if (groups.isEmpty()) {
			log.info(String.format("No records for export batch %s", batch.getBatchId()));
		}
		//TODO - talk with Siegi
		// it seems that the list is filled with random elements, this could also lead to not reproduceable exports (after an update of an exposure)
		ensureMinNumExposures(exposures, batch.getRegion(), exportProperties.getMinRecords(), exportProperties.getPaddingRange());

		// Load the non-expired signature infos associated with this export batch. - in our case we already have them, just have to filter them	
		List<SignatureInfo> sigInfos = batch.getSignatureInfos().stream().filter(si -> si.getEndTimestamp() == null || !si.getEndTimestamp().isBefore(LocalDateTime.now())).collect(Collectors.toList());

		// Create the export files.
		int batchSize = groups.size();
		List<String> objectNames = new ArrayList<>();

		for (int i = 0; i < groups.size(); i++) {
			List<Exposure> group = groups.get(i);
			String objectName = blobstoreService.createFile(batch, exposures, i + 1, batchSize, sigInfos);
			log.info(String.format("Wrote export file %s for batch %s", objectName, batch.getBatchId()));
			objectNames.add(objectName);
		}		
		// Emit the index file if needed.
		if (batchSize > 0 || emitIndexForEmptyBatch)  {
			retryingCreateIndex(batch, objectNames);
		}

		finalizeBatch(batch, objectNames , batchSize);
		
		log.info(String.format("Batch %s completed", batch.getBatchId()));		
		
	}

	private void ensureMinNumExposures(List<Exposure> exposures, String region, Integer minRecords, Integer paddingRange) {
		//TODO - check with Siegi - here we should add some random valoes
	}

	// FinalizeBatch writes the ExportFile records and marks the ExportBatch as complete.
	private void finalizeBatch(ExportBatch batch, List<String> files, int batchSize) {
		for (int i = 0; i < files.size(); i++) {
			String file = files.get(i);
			ExportFile exportFile = new ExportFile(file, batch.getBucketName(), batch, batch.getRegion(), i +1, batchSize, ExportBatchStatus.EXPORT_BATCH_COMPLETE);
			if (exportFileRepository.findById(file).isPresent()) {
				log.info(String.format("ExportFile %s already exists in database, skipping without overwriting. This can occur when reprocessing a failed batch.", file));
			} else {
				exportFileRepository.save(exportFile);
			}
		}
		// Update ExportBatch to mark it complete.
		batch.setStatus(ExportBatchStatus.EXPORT_BATCH_COMPLETE);
		batchService.saveBatch(batch);
	}
	
	
	
	
	// retryingCreateIndex create the index file. The index file includes _all_ batches for an ExportConfig,
	// so multiple workers may be racing to update it. We use a lock to make them line up after one another.
	
	//TODO - check with Siegi if we have concurrency
	private void retryingCreateIndex(ExportBatch batch, List<String> objectNames) {
		//TODO - implementation needed
	}


}
