package at.roteskreuz.covidapp.domain;

import at.roteskreuz.covidapp.convert.StringToListConverter;
import java.io.Serializable;
import java.time.Duration;
import java.util.List;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * AuthorizedApp represents the configuration for a single exposure notification
 * application and their access to and requirements for using the API. DB times
 * of 0 are interpreted to be "unbounded" in that direction.
 *
 * @author Zoltán Puskai
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class AuthorizedApp implements Serializable {

	// AppPackageName is the name of the package like com.company.app.
	@Id
	private String appPackageName;

	// Platform is the app platform like "android" or "ios".
	private String platform;

	// AllowedRegions is the list of allowed regions for this app. If the list is
	// empty, all regions are permitted.
	@Convert(converter = StringToListConverter.class)
	List<String> allowedRegions;

	// SafetyNet configuration.
	private String safetyNetApkDigestSHA256;

	private boolean safetyNetBasicIntegrity;

	private boolean safetyNetCTSProfileMatch;

	private Duration safetyNetPastTime;

	private Duration safetyNetFutureTime;

	// DeviceCheck configuration.
	private String deviceCheckKeyID;

	private String deviceCheckTeamID;

	private String deviceCheckPrivateKey;

	public boolean isRegionAllowed(String region) {
		if (allowedRegions.isEmpty()) {
			return true;
		}
		return allowedRegions.contains(region.toUpperCase());
	}

}
