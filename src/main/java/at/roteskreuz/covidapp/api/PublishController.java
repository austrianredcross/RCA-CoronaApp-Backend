package at.roteskreuz.covidapp.api;


import at.roteskreuz.covidapp.exception.InvalidTanException;
import at.roteskreuz.covidapp.model.ApiResponse;
import at.roteskreuz.covidapp.model.Publish;
import at.roteskreuz.covidapp.properties.PublishProperties;
import at.roteskreuz.covidapp.service.PublishService;
import at.roteskreuz.covidapp.service.TanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller for storing published exposure keys
 *
 * @author Zoltán Puskai
 */
@RestController
@RequestMapping(path = "/api/v${application.api.version}")
@RequiredArgsConstructor
@Api(tags = "Publish", description = "Endpoint storing infection information.")
public class PublishController {

	private final PublishService publishService;
	private final TanService tanService;
	private final PublishProperties publishProperties;


	/**
	 * Stores exposures published by the clients
	 * @param publish request containing exposures and validation data
	 * @return Api response
	 * @throws InvalidTanException if the Tan validation fails
	 */
	@PostMapping(value = "/publish", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Publishes infection information.", authorizations = {
		@Authorization(value = "AuthorizationKey")})
	@ApiImplicitParams({
	   @ApiImplicitParam(name = "X-AppId", value = "Application id", required = true, dataType = "string", paramType = "header")
	 })
	public ResponseEntity<ApiResponse> publish(@Valid @RequestBody Publish publish) throws InvalidTanException {
		if (!publishProperties.isBypassTanValidation() && !tanService.validate(publish.getVerificationPayload().getUuid(), publish.getVerificationPayload().getAuthorization(), publish.getDiagnosisType())) {
			throw new InvalidTanException(String.format("TAN is invalid. tan: %s, uuid:%s, type: %s", publish.getVerificationPayload().getUuid(), publish.getVerificationPayload().getAuthorization(), publish.getDiagnosisType()));
		}
		ApiResponse response = publishService.publish(publish);
		return ResponseEntity.status(response.getStatus()).body(response);
	}

}
