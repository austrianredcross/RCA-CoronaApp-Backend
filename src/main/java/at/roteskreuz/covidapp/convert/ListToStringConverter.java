package at.roteskreuz.covidapp.convert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Converter class for converting list of String into String and  the other way around
 *
 * @author Zoltán Puskai
 */
@Converter
public class ListToStringConverter implements AttributeConverter<List<String>, String> {

	/**
	 * Converts list of Strings to database column
	 * @param list the list to be converted
	 * @return value to be stored in database
	 */
	@Override
	public String convertToDatabaseColumn(List<String> list) {
		if (list == null) {
			return null;
		}
		return String.join(",", list);
	}
	/**
	 * Splits the String into a list
	 * @param joined The String to be split
	 * @return a list from the database field value
	 */
	@Override
	public List<String> convertToEntityAttribute(String joined) {
		if (joined == null) {
			return Collections.EMPTY_LIST;
		}
		return new ArrayList<>(Arrays.asList(joined.split(",")));
	}

}
