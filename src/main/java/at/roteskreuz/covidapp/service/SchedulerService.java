package at.roteskreuz.covidapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Service for flushing the client configuration cache periodically
 * client.configuration.cache.ttl -  the number of milliseconds between 2 calls can be configured in the application.properties
 * 
 * @author Zoltán Puskai
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerService {
	
	private final ClientConfigurationService clientConfigService;
	private final BatchService batchService;
	private final WorkerService workerService;
	private final CleanupService cleanupService;
			
	
	/**
	 * Flushes the client configuration cache
	 */
	@Scheduled(fixedRateString ="${application.schedule.client.config.cache.ttl}", initialDelayString="${application.schedule.client.config.cache.ttl}")
	public void flushCache() {
		log.debug("Flushing the client configuration cache");
		clientConfigService.flushCache();
	}

	
	
	@Scheduled(cron = "${application.schedule.cron.create.batches}")
	public void createBatches() {
		log.debug("Creating batches");
		batchService.createBratches();
	}
	
	@Scheduled(cron = "${application.schedule.cron.export.files}")
	public void exportFiles() throws Exception {
		log.debug("Exporting files");
		workerService.doWork();
	}

	@Scheduled(cron = "${application.schedule.cron.cleanup.files}")
	public void cleanupFiles() {
		log.debug("Cleaning up files");
		cleanupService.cleanupExport();
	}

	@Scheduled(cron = "${application.schedule.cron.cleanup.exposures}")
	public void cleanupExposures() {
		log.debug("Cleaning up exposures");
		cleanupService.cleanupExposure();
	}
	
}
