package at.roteskreuz.covidapp.admin;


import at.roteskreuz.covidapp.model.ApiResponse;
import at.roteskreuz.covidapp.service.ExportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller for exporting files
 *
 * @author Zoltán Puskai
 */
@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
@Api(tags = "Export", description = "Endpoint that exports files.")
public class ExportController {
	
	private final ExportService exportService;

	/**
	 * Exports exposures
	 * @return Api response
	 * @throws Exception 
	 */
	@ApiOperation(value = "Creates the export files.)", authorizations = {
		@Authorization(value = "AuthorizationKey")})
	@ApiImplicitParams({
	   @ApiImplicitParam(name = "X-AppId", value = "Application id", required = true, dataType = "string", paramType = "header")		
	 })		
	@ApiResponses(value = {
		@io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = ApiResponse.class),
		@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request"),
		@io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden"),
		@io.swagger.annotations.ApiResponse(code = 500, message = "Internal server error")})	
	@GetMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse> export() throws Exception  {
		ApiResponse response = exportService.export();
		return ResponseEntity.status(response.getStatus()).body(response);
	}	
	
}
