package at.roteskreuz.covidapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Value;

/**
 *
 * @author Zoltán Puskai
 */
@Value
public class IndexFileBatch {
	private long interval;
	@JsonProperty("batch_file_paths")
	private List<String> batchFilePaths;
}
