package at.roteskreuz.covidapp.repository;

import at.roteskreuz.covidapp.domain.Exposure;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository for persisting authorized apps
 * 
 * @author Zoltán Puskai
 * @since 0.0.1-SNAPSHOT
 */

public interface ExposureRepository extends CrudRepository<Exposure, Long>, JpaSpecificationExecutor<Exposure> {

    @Transactional
    List<Exposure> deleteAllByCreatedAtIsLessThan(LocalDateTime createdAt);
	
}
