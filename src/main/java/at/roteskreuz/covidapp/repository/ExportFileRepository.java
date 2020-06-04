package at.roteskreuz.covidapp.repository;

import at.roteskreuz.covidapp.domain.ExportBatch;
import at.roteskreuz.covidapp.domain.ExportConfig;
import at.roteskreuz.covidapp.domain.ExportFile;
import at.roteskreuz.covidapp.domain.ExportFilename;
import at.roteskreuz.covidapp.model.ExportBatchStatus;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for persisting exported file related information
 * 
 * @author Zoltán Puskai
 */
public interface ExportFileRepository extends CrudRepository<ExportFile, String> {

	/**
	 * Finds files for a batch where the status of the file is not deleted
	 * @param batch
	 * @param status
	 * @return 
	 */
    List<ExportFile> findAllByBatchIsAndStatusIsNot(ExportBatch batch, ExportBatchStatus status);
	
	/**
	 * Finds export files for the given ExportConfig with batch status completed
	 * @param config
	 * @param status
	 * @return 
	 */
	List<ExportFilename> findByBatchConfigAndBatchStatusOrderByFilename(ExportConfig config, ExportBatchStatus status);
	
}
