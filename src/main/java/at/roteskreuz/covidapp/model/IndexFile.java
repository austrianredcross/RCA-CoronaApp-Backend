package at.roteskreuz.covidapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author zolika
 */
@Getter
@Setter
public class IndexFile {
	@JsonProperty("full_14_batch")
	private IndexFileBatch fullBigBatch;
	@JsonProperty("full_7_batch")
	private IndexFileBatch fullMediumBatch;
	@JsonProperty("daily_batches")
	private List<IndexFileBatch> dailyBatches;
}
