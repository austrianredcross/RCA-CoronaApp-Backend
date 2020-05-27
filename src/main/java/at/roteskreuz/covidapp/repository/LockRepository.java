package at.roteskreuz.covidapp.repository;

import at.roteskreuz.covidapp.domain.Lock;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for persisting authorized apps
 * 
 * @author Zoltán Puskai
 * @since 0.0.1-SNAPSHOT
 */

public interface LockRepository extends CrudRepository<Lock, String> {


}
