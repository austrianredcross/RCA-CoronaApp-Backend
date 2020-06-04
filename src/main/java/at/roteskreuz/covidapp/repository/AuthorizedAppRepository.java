package at.roteskreuz.covidapp.repository;

import at.roteskreuz.covidapp.domain.AuthorizedApp;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for persisting authorized apps
 * 
 * @author Zoltán Puskai
 */
public interface AuthorizedAppRepository extends CrudRepository<AuthorizedApp, String> {

	
}
