package at.roteskreuz.covidapp.repository;

import at.roteskreuz.covidapp.domain.ExportConfig;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for persisting export configuration
 *
 * @author Zoltán Puskai
 */
public interface ExportConfigRepository extends CrudRepository<ExportConfig, Long> {

	/**
	 * Finds export configurations whose FromTimestamp is before the given time
	 * @param timestamp
	 * @return
	 */
	@Query("SELECT e FROM ExportConfig e WHERE e.from < ?1 and (e.thru IS NULL OR e.thru > ?1)")
	List<ExportConfig> findAllByDate(LocalDateTime timestamp);

}
