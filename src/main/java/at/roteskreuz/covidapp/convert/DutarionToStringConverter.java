package at.roteskreuz.covidapp.convert;

import java.time.Duration;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Converter class for converting time duration into String and  the other way around
 *
 * @author Zoltán Puskai
 */
@Converter
public class DutarionToStringConverter implements AttributeConverter<Duration, String> {

	/**
	 * Converts duration to database column
	 * @param duration the duration to be converted
	 * @return value to be stored in database
	 */
	@Override
	public String convertToDatabaseColumn(Duration duration) {
		if (duration == null) {
			return null;
		}
		return duration.toString();
	}
	/**
	 * Converts String to a time duration
	 * @param duration duration string
	 * @return a list from the database field value
	 */
	@Override
	public Duration convertToEntityAttribute(String duration) {
		try {
			return Duration.parse(duration);		
		} catch (Exception e) {
		}
		return null;
	}

}
