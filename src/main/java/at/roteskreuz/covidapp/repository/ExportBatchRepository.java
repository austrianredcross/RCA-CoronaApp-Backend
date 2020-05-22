package at.roteskreuz.covidapp.repository;

import at.roteskreuz.covidapp.domain.ExportBatch;
import at.roteskreuz.covidapp.domain.ExportConfig;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for persisting authorized apps
 * 
 * @author Zoltán Puskai
 * @since 0.0.1-SNAPSHOT
 */

public interface ExportBatchRepository extends CrudRepository<ExportBatch, Long> {

	ExportBatch findTop1ByConfigOrderByEndTimestampDesc(ExportConfig config);
	
}
