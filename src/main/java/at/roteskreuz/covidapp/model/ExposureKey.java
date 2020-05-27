package at.roteskreuz.covidapp.model;

import at.roteskreuz.covidapp.validation.ValidExposureKey;
import java.util.Base64;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author zolika
 */
@Getter
@Setter
@NoArgsConstructor
@ValidExposureKey
public class ExposureKey {
	
	private String key;
	
	//@Min(1000)
	private Integer intervalNumber;

	private Integer intervalCount;
	
	private Integer transmissionRisk;
	
	
	public String getBinKey() {
		if (key == null) {
			return null;
		}
		return new String(Base64.getDecoder().decode(key.getBytes()));
	}

}