package at.roteskreuz.covidapp.repository;

import at.roteskreuz.covidapp.domain.ExportConfig;
import at.roteskreuz.covidapp.domain.ExportFile;
import at.roteskreuz.covidapp.model.ExportFileStatus;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for persisting exported file related information
 * 
 * @author Zoltán Puskai
 */
public interface ExportFileRepository extends CrudRepository<ExportFile, String> {

	/**
	 * Finds export files for the given ExportConfig
	 * @param config
	 * @param timestamp
	 * @param status
	 * @return 
	 */
	List<ExportFile> findByConfigAndTimestampLessThanAndStatusIsNot(ExportConfig config, Long timestamp, ExportFileStatus status);
	
}
