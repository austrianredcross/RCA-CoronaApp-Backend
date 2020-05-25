package at.roteskreuz.covidapp.domain;

import com.microsoft.applicationinsights.core.dependencies.apachecommons.lang3.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author zolika
 */
@RequiredArgsConstructor
public class ExposureSpecificationsBuilder {

	private final ExposureCriteria criteria;
	
	public Specification<Exposure> build() {
		return inRegions().and(afterSince()).and(beforeUntis()).and(isLocalProvenance());
	}

	private Specification<Exposure> inRegions() {
		return (root, queryBuilder, criteriaBuilder) -> {
			if (StringUtils.isNotBlank(criteria.getRegion())) {
				return	criteriaBuilder.like(root.get("regions"), "%," + criteria.getRegion() + ",%");
			} else {
				return criteriaBuilder.and();
			}
		};
	}
	
	private Specification<Exposure> afterSince() {
		return (root, queryBuilder, criteriaBuilder) -> {
			if (criteria.getSinceTimestamp() != null) {
				return	criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), criteria.getSinceTimestamp());
			} else {
				return criteriaBuilder.and();
			}
		};
	}
	
	private Specification<Exposure> beforeUntis() {
		return (root, queryBuilder, criteriaBuilder) -> {
			if (criteria.getUntilTimestamp() != null) {
						return	criteriaBuilder.lessThan(root.get("createdAt"), criteria.getUntilTimestamp());
			} else {
				return criteriaBuilder.and();
			}
		};
	}	
	private Specification<Exposure> isLocalProvenance() {
		return (root, queryBuilder, criteriaBuilder) -> {
			if (criteria.getOnlyLocalProvenance() != null && criteria.getOnlyLocalProvenance()) {
				return	criteriaBuilder.equal(root.get("localProvenance"), true);
			} else {
				return criteriaBuilder.and();
			}
		};
	}		
	

}
