package at.roteskreuz.covidapp.domain;

import at.roteskreuz.covidapp.model.ExportBatchStatus;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
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
public class ExportFile implements Serializable {

	@Id
	private Long id;

	private String bucketName;
	private String filename;
	@OneToOne
	private ExportBatch batch;
	private String region;
	private Integer batchNum;
	private Integer batchSize;
	private ExportBatchStatus status;

}
