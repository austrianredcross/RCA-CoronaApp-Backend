package at.roteskreuz.covidapp.service;

import java.time.Duration;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/*
 * @author Zoltán Puskai
 */
@SpringBootTest
public class TimeCalculationServiceTest {

	@Autowired
	private TimeCalculationService service;

	@Test
	public void truncatedToMinutesShouldNotHaveSeconds() {
		assertThat(service.truncateToDuration(LocalDateTime.now(), Duration.ofMinutes(1)).getSecond() == 0);
	}

	@Test
	public void truncatedToHoursShouldNotHaveMinutes() {
		assertThat(service.truncateToDuration(LocalDateTime.now(), Duration.ofHours(1)).getMinute() == 0);

	}
	
	
}
