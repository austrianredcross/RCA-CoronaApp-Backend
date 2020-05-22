package at.roteskreuz.covidapp.repository;

import at.roteskreuz.covidapp.domain.Exposure;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for persisting authorized apps
 * 
 * @author Zoltán Puskai
 * @since 0.0.1-SNAPSHOT
 */

public interface ExposureRepository extends CrudRepository<Exposure, Long> {

	
}
