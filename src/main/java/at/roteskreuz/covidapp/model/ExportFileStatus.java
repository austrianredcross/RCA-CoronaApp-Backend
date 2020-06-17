package at.roteskreuz.covidapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The different states of export batches
 * 
 * @author Zoltán Puskai
 */
@AllArgsConstructor
@Getter
public enum ExportFileStatus {

	EXPORT_FILE_CREATED ("CREATED"),
	EXPORT_FILE_DELETED ("DELETED");
	
	private String value;
	
}
