package at.roteskreuz.covidapp.api;

import at.roteskreuz.covidapp.model.ApiResponse;
import at.roteskreuz.covidapp.service.PushNotificationService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Controller for Push Notifications
 *
 * @author Bernhard Roessler
 * @since 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping(path = "/api/v${application.api.version}")
@RequiredArgsConstructor
@Api(tags = "PushNotification", description = "Endpoint for Push Notifications.")
public class PushNotificationController {

	private final PushNotificationService pushNotificationService;

	@GetMapping(value = "/push-notification", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> pushNotification()  {
		return pushNotificationService.sendNotification(UUID.randomUUID().toString()) ?
						ResponseEntity.ok().build() :
						ResponseEntity.badRequest().build();
	}

}
