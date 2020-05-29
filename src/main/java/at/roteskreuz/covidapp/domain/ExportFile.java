package at.roteskreuz.covidapp.domain;

import at.roteskreuz.covidapp.model.ExportBatchStatus;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ExportBatch represents a a file from exposures export.
 *
 * @author Zoltán Puskai
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExportFile implements Serializable {

	@Id
	private String filename;
	private String bucketName;
	@OneToOne
	private ExportBatch batch;
	private String region;
	private Integer batchNum;
	private Integer batchSize;
	private ExportBatchStatus status;

}
