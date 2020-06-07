package at.roteskreuz.covidapp.repository;

import at.roteskreuz.covidapp.domain.ExportConfig;
import at.roteskreuz.covidapp.domain.ExportFile;
import at.roteskreuz.covidapp.domain.ExportTimestamp;
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
	 * @return 
	 */
	List<ExportTimestamp> findByConfigAndTimestampLessThan(ExportConfig config, Long timestamp);
	
}
