package at.roteskreuz.covidapp.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
	@ManyToOne
	private ExportConfig config;
	private String region;
	private long timestamp;
}
