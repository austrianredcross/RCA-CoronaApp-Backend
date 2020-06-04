package at.roteskreuz.covidapp.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * SignatureInfo represents signature information used when signing the exported files
 *
 * @author Zoltán Puskai
 */	
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignatureInfo implements Serializable {

	@Id
	private Long id;
	private String signingKey;
	private String appPackageName;
	private String bundleID;
	private String signingKeyVersion;
	
	private String signingKeyID;
	private LocalDateTime endTimestamp;
	
}
