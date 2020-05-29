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
public enum ExportBatchStatus {

	EXPORT_BATCH_OPEN("OPEN"),
	EXPORT_BATCH_PENDING("PENDING"),
	EXPORT_BATCH_COMPLETE("COMPLETE"),
	EXPORT_BATCH_DELETED("DELETED");
	
	private String value;
	
}
