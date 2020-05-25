package at.roteskreuz.covidapp.repository;

import at.roteskreuz.covidapp.domain.ExportBatch;
import at.roteskreuz.covidapp.domain.ExportConfig;
import at.roteskreuz.covidapp.model.ExportBatchStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for persisting authorized apps
 * 
 * @author Zoltán Puskai
 * @since 0.0.1-SNAPSHOT
 */

public interface ExportBatchRepository extends CrudRepository<ExportBatch, Long> {

	ExportBatch findTop1ByConfigOrderByEndTimestampDesc(ExportConfig config);
	
	
	@Query("SELECT b.batchId FROM ExportBatch b WHERE (b.status = ?1 OR (b.status = ?2 AND leaseExpires < ?3)) AND b.endTimestamp < ?3")
	List<Long> leaseBatches(ExportBatchStatus openStatus, ExportBatchStatus pendingStatus, LocalDateTime timestamp, Pageable pageable);
	
	
}
