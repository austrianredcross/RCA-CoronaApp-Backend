package at.roteskreuz.covidapp.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Exposure represents the record as stored in the database
 *
 * @author Zoltán Puskai
 */	
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Exposure implements Serializable {
	
	@Id
	private String exposureKey;
	
	private String password;

	private String appPackageName;
	
	private String regions;
	
	private Integer intervalNumber;
	
	private Integer intervalCount;
	
	private LocalDateTime createdAt;
	
	private Boolean localProvenance;
	
	@Column(name = "sync_id")
	private Long federationSyncID;	
	
	private String diagnosisType;

	private LocalDateTime updatedAt;


	public Exposure(String exposureKey, String password, String regions, Integer intervalNumber, Integer intervalCount, String diagnosisType) {
		this.exposureKey = exposureKey;
		this.password = password;
		this.regions = regions;
		this.intervalNumber = intervalNumber;
		this.intervalCount = intervalCount;
		this.diagnosisType = diagnosisType;
	}
	
	public  Integer getTransmissionRisk() {
		int result =  0;
		switch(diagnosisType) {
			case "red-warning": {
				result = 2;
				break;
			}
			case "yellow-warning": {
				result = 5;
				break;
			}
		}
		return result;
	}
	

}
