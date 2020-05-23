package at.roteskreuz.covidapp.repository;

import at.roteskreuz.covidapp.domain.ClientConfiguration;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for persisting client configuration
 * 
 * @author Zoltán Puskai
 * @since 0.0.1-SNAPSHOT
 */

public interface ClientConfigurationRepository extends CrudRepository<ClientConfiguration, Long> {

	
}
