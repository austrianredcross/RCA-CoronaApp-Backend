package at.roteskreuz.covidapp.repository;

import at.roteskreuz.covidapp.domain.ExportFile;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for persisting authorized apps
 * 
 * @author Zoltán Puskai
 * @since 0.0.1-SNAPSHOT
 */

public interface ExportFileRepository extends CrudRepository<ExportFile, String> {

	
}
