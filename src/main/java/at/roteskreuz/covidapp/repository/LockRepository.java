package at.roteskreuz.covidapp.repository;

import at.roteskreuz.covidapp.domain.Lock;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for persisting locks
 * 
 * @author Zoltán Puskai
 */
public interface LockRepository extends CrudRepository<Lock, String> {


}
