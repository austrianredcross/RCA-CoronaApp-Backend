package at.roteskreuz.covidapp.domain;

import at.roteskreuz.covidapp.convert.StringToListConverter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author zolika
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Exposure implements Serializable {
	
	@Id
	private String exposureKey;

	private Integer transmissionRisk;
	
	private String appPackageName;
	
	@Convert(converter = StringToListConverter.class)
	List<String> regions;
	
	private Integer intervalNumber;
	
	private Integer intervalCount;
	
	private LocalDateTime createdAt;
	
	private Boolean localProvenance;
	
	@Column(name = "sync_id")
	private Long federationSyncID;

}
