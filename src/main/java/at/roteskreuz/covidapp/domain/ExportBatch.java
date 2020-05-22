package at.roteskreuz.covidapp.domain;

import at.roteskreuz.covidapp.model.ExportBatchStatus;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
public class ExportBatch implements Serializable {

	@Id
	private Long batchId;
	private ExportConfig config;
	private String bucketName;
	private String filenameRoot;
	private LocalDateTime startTimestamp;
	private LocalDateTime endTimestamp;
	private String region;
	private ExportBatchStatus status;
	private LocalDateTime leaseExpires;
	
	@OneToMany
	private List<SignatureInfo> signatureInfos;

}
