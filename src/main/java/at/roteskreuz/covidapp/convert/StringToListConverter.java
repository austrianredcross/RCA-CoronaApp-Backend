package at.roteskreuz.covidapp.convert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author zolika
 */
@Converter
public class StringToListConverter implements AttributeConverter<List<String>, String> {

	@Override
	public String convertToDatabaseColumn(List<String> list) {
		if (list == null) {
			return null;
		}
		return String.join(",", list);
	}

	@Override
	public List<String> convertToEntityAttribute(String joined) {
		if (joined == null) {
			return Collections.EMPTY_LIST;
		}
		return new ArrayList<>(Arrays.asList(joined.split(",")));
	}

}
