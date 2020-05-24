package at.roteskreuz.covidapp.service;

import at.roteskreuz.covidapp.domain.ExportBatch;
import at.roteskreuz.covidapp.domain.ExportConfig;
import at.roteskreuz.covidapp.model.ApiResponse;
import at.roteskreuz.covidapp.model.BatchRange;
import at.roteskreuz.covidapp.model.ExportBatchStatus;
import at.roteskreuz.covidapp.repository.ExportBatchRepository;
import at.roteskreuz.covidapp.repository.ExportConfigRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author zolika
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BatchService {

	@Value("${export.truncate-window:Hours}")
	private ChronoUnit truncateWindow;

	private static final LocalDateTime SANITY_DATE = LocalDateTime.of(2019, 1, 1, 0, 0, 0, 0);

	private final ExportConfigRepository exportConfigRepository;
	private final ExportBatchRepository exportBatchRepository;

	public ApiResponse createBratches() {
		LocalDateTime now = LocalDateTime.now();

		int totalConfigs = 0;
		int totalBatches = 0;
		int totalConfigsWithBatches = 0;

		List<ExportConfig> exportConfigs = exportConfigRepository.findAllByDate(now);
		for (ExportConfig exportConfig : exportConfigs) {
			totalConfigs++;
			int batchesCreated = maybeCreateBatches(exportConfig, now);
			totalBatches += batchesCreated;
			if (batchesCreated > 0) {
				totalConfigsWithBatches++;
			}
		}
		log.info(String.format("Processed %s configs creating %s batches across %s configs", totalConfigs, totalBatches, totalConfigsWithBatches));
		return ApiResponse.ok();
	}

	private int maybeCreateBatches(ExportConfig exportConfig, LocalDateTime now) {
		LocalDateTime latestEnd = latestExportBatchEnd(exportConfig);

		List<BatchRange> ranges = makeBatchRanges(exportConfig.getPeriod(), latestEnd, now, truncateWindow);

		if (ranges == null || ranges.isEmpty()) {
			log.debug("Batch creation for config %d is not required, skipping", exportConfig.getConfigID());
			return 0;
		}
		ranges.forEach(r -> {
			exportBatchRepository.save(new ExportBatch(null,
						exportConfig,
						exportConfig.getBucketName(),
						exportConfig.getFilenameRoot(),
						r.getStart(), r.getEnd(),
						exportConfig.getRegion(),
						ExportBatchStatus.EXPORT_BATCH_OPEN,
						null,
						new ArrayList<>(exportConfig.getSignatureInfos())));		
		});
				

		log.info(String.format("Created %s batch(es) for config %s", ranges.size(), exportConfig.getConfigID()));
		return ranges.size();
	}

	private List<BatchRange> makeBatchRanges(Duration period, LocalDateTime latestEnd, LocalDateTime now, ChronoUnit truncateWindow) {
		// Compute the end of the exposure publish window; we don't want any batches with an end date greater than this time.
		LocalDateTime publishEnd = now.truncatedTo(truncateWindow);
		// Special case: if there have not been batches before, return only a single one.
		// We use sanityDate here because the loop below will happily create batch ranges
		// until the beginning of time otherwise.		
		if (latestEnd.isBefore(SANITY_DATE)) {
			// We want to create a batch aligned on the period, but not overlapping the current publish window.
			// To do this, we use the publishEnd and truncate it to the period; this becomes the end date.
			// Then we just subtract the period to get the start date.
			LocalDateTime end = publishEnd.truncatedTo(ChronoUnit.HOURS);//should be truncated with period
			LocalDateTime start = end.minus(period);
			return Arrays.asList(new BatchRange(start, end));
		}

		// Truncate now to align with period; use this as the end date.
		LocalDateTime end = now.truncatedTo(ChronoUnit.HOURS);//should be truncated with period

		// If the end date < latest end date, we already have a batch that covers this period, so return no batches.
		if (end.isBefore(latestEnd)) {
			return null;
		}

		// Subtract period to get the start date.
		LocalDateTime start = end.minus(period);

		// Build up a list of batches until we reach that latestEnd.
		// Allow for overlap so we don't miss keys; this might happen in the event that
		// an ExportConfig was edited and the new settings don't quite align.	
		List<BatchRange> ranges = new ArrayList<>();
		while (end.isAfter(latestEnd)) {
			// If the batch's end is after the publish window, don't add this range.
			if (!end.isAfter(publishEnd)) {
				ranges.add(new BatchRange(start, end));
			}
			start = start.minus(period);
			end = end.minus(period);
		}
		return ranges;
	}

	private LocalDateTime latestExportBatchEnd(ExportConfig exportConfig) {
		ExportBatch exportBatch = exportBatchRepository.findTop1ByConfigOrderByEndTimestampDesc(exportConfig);
		if (exportBatch != null) {
			return exportBatch.getEndTimestamp();
		}
		return LocalDateTime.MIN;
	}

	public ApiResponse doWork() {
		return ApiResponse.ok();
	}

}
