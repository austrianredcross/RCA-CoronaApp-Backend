package at.roteskreuz.covidapp.repository;

import at.roteskreuz.covidapp.domain.ClientConfiguration;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for persisting client configuration
 *
 * @author Zoltán Puskai
 */
public interface ClientConfigurationRepository extends CrudRepository<ClientConfiguration, Long> {


}
