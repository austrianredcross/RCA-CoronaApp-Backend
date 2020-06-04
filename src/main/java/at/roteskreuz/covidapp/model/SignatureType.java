package at.roteskreuz.covidapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The different types of signatures
 * 
 * @author Zoltán Puskai
 */
@AllArgsConstructor
@Getter
public enum SignatureType {
	NONE("none"),
	FILESYSTEM("filesystem"),
	AZURE("azure");
	
	private final String name;
}
