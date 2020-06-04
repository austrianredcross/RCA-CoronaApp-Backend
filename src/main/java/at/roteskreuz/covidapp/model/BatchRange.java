package at.roteskreuz.covidapp.model;

import java.time.LocalDateTime;
import lombok.Value;

/**
 * BatchRange represents a rang for batches
 * 
 * @author Zoltán Puskai
 */
@Value
public class BatchRange {
	
	private final LocalDateTime start;
	private final LocalDateTime end;

}
