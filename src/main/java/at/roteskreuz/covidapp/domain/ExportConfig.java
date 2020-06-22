package at.roteskreuz.covidapp.domain;

import at.roteskreuz.covidapp.convert.DutarionToStringConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ExportConfig represents configuration for exporting exposures.
 *
 * @author Zoltán Puskai
 */
@Slf4j
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExportConfig implements Serializable {

	@Id
	@Column(name = "configid")
	private Long id;
	private String bucketName;
	private String filenameRoot;

	@Convert(converter = DutarionToStringConverter.class)
	private Duration periodRedWarnings;

	@Convert(converter = DutarionToStringConverter.class)
	private Duration periodYellowWarnings;

	@Convert(converter = DutarionToStringConverter.class)
	private Duration periodOfKeepingFiles;

	@Convert(converter = DutarionToStringConverter.class)
	private Duration periodOfDailyFiles;

	@Convert(converter = DutarionToStringConverter.class)
	private Duration periodOfBigFile;

	@Convert(converter = DutarionToStringConverter.class)
	private Duration periodOfMediumFile;

	private String region;
	@Column(name="from_timestamp")
	private LocalDateTime from;
	@Column(name="thru_timestamp")
	private LocalDateTime thru;
	@ManyToMany(fetch = FetchType.EAGER)
	private List<SignatureInfo> signatureInfos;

	@JsonIgnore
	public Duration getExposureCleanupPeriod() {
		Duration result;
		if (periodRedWarnings == null || periodYellowWarnings == null) {
			log.warn(String.format("Periods are not configured well config with id: %d", this,getId()));
			result = Duration.ZERO;
		} else {
			result = periodRedWarnings.compareTo(periodYellowWarnings) > 0 ? periodRedWarnings: periodYellowWarnings;
		}
		return result;
	}

}
