package at.roteskreuz.covidapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author zolika
 */
@AllArgsConstructor
@Getter
public enum BlobstoreType {
	NONE("none"),
	FILESYSTEM("filesystem"),
	AZURE_CLOUD_STORAGE("azure-cloud-storage");
	
	private final String name;
}
