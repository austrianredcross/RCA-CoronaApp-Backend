package at.roteskreuz.covidapp.admin;

import at.roteskreuz.covidapp.service.PushNotificationService;
import io.swagger.annotations.Api;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for Push Notifications
 *
 * @author Bernhard Roessler
 */
@RestController
@RequestMapping(path = "/admin/push")
@RequiredArgsConstructor
@Api(tags = "PushNotification", description = "Endpoint for Push Notifications.")
public class PushNotificationController {

	private final PushNotificationService pushNotificationService;


	/**
	 * Sends out push notifications
	 * @return 200 (OK) or 400 (Bad request), if notification cannot be sent
	 */
	@GetMapping(value = "/notification", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> pushNotification()  {
		return pushNotificationService.sendNotification(UUID.randomUUID().toString()) ?
						ResponseEntity.ok().build() :
						ResponseEntity.badRequest().build();
	}

}
