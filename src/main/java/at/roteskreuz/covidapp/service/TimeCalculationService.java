package at.roteskreuz.covidapp.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Service;

/**
 *
 * @author zolika
 */
@Service
public class TimeCalculationService {

	public LocalDateTime truncateToDuration(LocalDateTime time, Duration duration) {
		LocalDateTime startOfDay = time.truncatedTo(ChronoUnit.DAYS);
		return startOfDay.plus(duration.multipliedBy(
				Duration.between(startOfDay, time).dividedBy(duration.getSeconds()).getSeconds()));
	}	
}
