package at.roteskreuz.covidapp.repository;

import at.roteskreuz.covidapp.domain.ExportBatch;
import at.roteskreuz.covidapp.domain.ExportConfig;
import at.roteskreuz.covidapp.domain.ExportFile;
import at.roteskreuz.covidapp.model.ExportBatchStatus;
import at.roteskreuz.covidapp.domain.ExportFilename;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for persisting authorized apps
 * 
 * @author Zoltán Puskai
 * @since 0.0.1-SNAPSHOT
 */

public interface ExportFileRepository extends CrudRepository<ExportFile, String> {

    List<ExportFile> findAllByBatchIsAndStatusIsNot(ExportBatch batch, ExportBatchStatus status);
	
	List<ExportFilename> findByBatchConfigAndBatchStatusOrderByFilename(ExportConfig config, ExportBatchStatus status);
	
}
