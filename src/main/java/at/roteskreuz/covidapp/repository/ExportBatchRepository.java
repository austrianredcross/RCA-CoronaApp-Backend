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
 * Repository for persisting export batches
 * 
 * @author Zoltán Puskai
 */
public interface ExportBatchRepository extends CrudRepository<ExportBatch, Long> {

	/**
	 * Finds the newest (by end timestamp) ExportBatch for an ExportConfig
	 * @param config 
	 * @return 
	 */
	ExportBatch findTop1ByConfigOrderByEndTimestampDesc(ExportConfig config);
	
	/**
	 *  Query for batches that are OPEN or PENDING with expired lease. Also, only return batches with end timestamp
	 * in the past (i.e., the batch is complete).
	 * @param openStatus batch  open status
	 * @param pendingStatus batch  pending status
	 * @param timestamp timestamp
	 * @param pageable pageable to limit the results to 100
	 * @return 
	 */
	@Query("SELECT b.batchId FROM ExportBatch b WHERE (b.status = ?1 OR (b.status = ?2 AND leaseExpires < ?3)) AND b.endTimestamp < ?3")
	List<Long> leaseBatches(ExportBatchStatus openStatus, ExportBatchStatus pendingStatus, LocalDateTime timestamp, Pageable pageable);

	/**
	 * Finds batches ending before the time passed in that are not deleted.
	 * @param endTimestamp timestamp
	 * @param status batch deleted status
	 * @return 
	 */
	List<ExportBatch> findByEndTimestampBeforeAndStatusIsNot(LocalDateTime endTimestamp, ExportBatchStatus status);
}
