package at.roteskreuz.covidapp.domain;

import at.roteskreuz.covidapp.model.ExportBatchStatus;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ExportBatch represents a  batch for exporting exposures.
 *
 * @author Zoltán Puskai
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExportBatch implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Long batchId;
	@ManyToOne
	private ExportConfig config;
	private String bucketName;
	private String filenameRoot;
	private LocalDateTime startTimestamp;
	private LocalDateTime endTimestamp;
	private String region;
	private ExportBatchStatus status;
	private LocalDateTime leaseExpires;
	
	@ManyToMany
	private List<SignatureInfo> signatureInfos;

}
