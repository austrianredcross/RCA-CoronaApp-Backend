package at.roteskreuz.covidapp.model;

import at.roteskreuz.covidapp.validation.ValidExposureKey;
import java.util.Base64;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representation of an exposure key in a publish request
 * 
 * @author Zoltán Puskai
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ValidExposureKey
public class ExposureKey {
	
	private String key;
	
	private Integer intervalNumber;

	private Integer intervalCount;
	
	private String password;
	
	
	public String binKey() {
		if (key == null) {
			return null;
		}
		return new String(Base64.getDecoder().decode(key.getBytes()));
	}

}
