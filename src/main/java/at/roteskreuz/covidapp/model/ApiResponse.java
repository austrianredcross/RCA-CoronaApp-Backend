package at.roteskreuz.covidapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author zolika
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

	private Integer status;
	
	private String message;
	
	private String metric;
	
	private Integer count;
	
	private Boolean errorInProd;

}
