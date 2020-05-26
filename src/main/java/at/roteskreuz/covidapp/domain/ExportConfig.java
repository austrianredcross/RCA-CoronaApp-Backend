package at.roteskreuz.covidapp.domain;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
public class ExportConfig implements Serializable {
	@Id
	private Long configID;
	private String bucketName;
	private String filenameRoot;
	private Duration period; // duration in seconds
	private String region;
	@Column(name="from_timestamp")
	private LocalDateTime from;
	@Column(name="thru_timestamp")
	private LocalDateTime thru;
	@ManyToMany
	private List<SignatureInfo> signatureInfos;

}
