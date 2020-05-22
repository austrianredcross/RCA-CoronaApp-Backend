package at.roteskreuz.covidapp.model;

import java.time.LocalDateTime;
import lombok.Value;

/**
 *
 * @author zolika
 */
@Value
public class BatchRange {
	
	private final LocalDateTime start;
	private final LocalDateTime end;

}
