package at.roteskreuz.covidapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The different types of blobstores
 * 
 * @author Zoltán Puskai
 */
@AllArgsConstructor
@Getter
public enum BlobstoreType {
	NONE("none"),
	FILESYSTEM("filesystem"),
	AZURE_CLOUD_STORAGE("azure-cloud-storage");
	
	private final String name;
}
