package at.roteskreuz.covidapp.domain;

import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Client configuration holds a JSON structure to configure Android and IOS clients
 * 
 * @author Zoltán Puskai
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class ClientConfiguration {
	
	/**
	 * Id of  the current configuration
	 */	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * Configuration in JSON format
	 */	
	//@Column(columnDefinition="TEXT")
	@Lob
	private String data;
	
	/**
	 * Date when the current configuration was created
	 */		
	private LocalDateTime dateCreated;	
	
}
