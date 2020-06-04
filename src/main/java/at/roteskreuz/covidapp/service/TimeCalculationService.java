package at.roteskreuz.covidapp.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Service;

/**
 * Service for date/time calculations
 * 
 * @author Zoltán Puskai
 */
@Service
public class TimeCalculationService {

	/**
	 * Truncates a time to a period
	 * @param time time to be truncated
	 * @param duration time period used for truncation
	 * @return 
	 */
	public LocalDateTime truncateToDuration(LocalDateTime time, Duration duration) {
		LocalDateTime startOfDay = time.truncatedTo(ChronoUnit.DAYS);
		return startOfDay.plus(duration.multipliedBy(
				Duration.between(startOfDay, time).dividedBy(duration.getSeconds()).getSeconds()));
	}	
}
