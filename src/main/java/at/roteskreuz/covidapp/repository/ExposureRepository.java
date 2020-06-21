package at.roteskreuz.covidapp.repository;

import at.roteskreuz.covidapp.domain.Exposure;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository for persisting exposures
 *
 * @author Zoltán Puskai
 */
public interface ExposureRepository extends CrudRepository<Exposure, String>, JpaSpecificationExecutor<Exposure> {

	/**
	 * finds exposures for a region in order to export them
	 * @param since since
	 * @param until until
	 * @param diagnosisType diagnosis type
	 * @param region region
	 * @return
	 */
	List<Exposure> findByIntervalNumberGreaterThanEqualAndIntervalNumberLessThanAndDiagnosisTypeAndRegionsLike(Integer since, Integer until, String diagnosisType, String region);



	/**
	 * Deletes all exposures older than..
	 * @param intervalNumber interval number
	 * @param region region
	 * @return
	 */
	//TODO - delete query with condition would perform better
    @Transactional
	List<Exposure> deleteAllByIntervalNumberIsLessThanAndRegionsLike(int intervalNumber, String region);

}
