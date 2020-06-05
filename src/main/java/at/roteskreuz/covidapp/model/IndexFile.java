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
	@JsonProperty("full_batch")
	private IndexFileBatch fullBatch;
	@JsonProperty("daily_batches")
	private List<IndexFileBatch> dailyBatches;
}
