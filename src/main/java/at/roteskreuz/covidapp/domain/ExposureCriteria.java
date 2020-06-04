package at.roteskreuz.covidapp.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * ExposureCriteria represents criteria for searching exposures
 *
 * @author Zoltán Puskai
 */	
@Getter
@Setter
@AllArgsConstructor
public class ExposureCriteria {
	
	private String region;
	
	private LocalDateTime sinceTimestamp;
	
	private LocalDateTime untilTimestamp;
	
	private Boolean onlyLocalProvenance;
	
	
}
