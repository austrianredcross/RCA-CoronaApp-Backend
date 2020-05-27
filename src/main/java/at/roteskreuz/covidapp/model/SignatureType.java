package at.roteskreuz.covidapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author zolika
 */
@AllArgsConstructor
@Getter
public enum SignatureType {
	NONE("none"),
	FILESYSTEM("filesystem"),
	AZURE("azure");
	
	private final String name;
}
