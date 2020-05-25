package at.roteskreuz.covidapp.validation;

import at.roteskreuz.covidapp.domain.AuthorizedApp;
import at.roteskreuz.covidapp.model.ExposureKey;
import at.roteskreuz.covidapp.model.Publish;
import at.roteskreuz.covidapp.service.AuthorizedAppService;
import at.roteskreuz.covidapp.service.DeviceCheckService;
import at.roteskreuz.covidapp.service.SafetynetAttestationService;
import java.util.Comparator;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * Validator for checking Publish objects
 *
 * @author Zoltán Puskai
 */
public class PublishValidator extends AbstractValidator implements ConstraintValidator<ValidPublish, Publish> {

	@Value("${application.publish.max-keys-on-publish}")
	private int maxExposureKeys;

	@Autowired
	private AuthorizedAppService authorizedAppService;
	@Autowired
	private DeviceCheckService deviceCheckService;
	@Autowired
	private SafetynetAttestationService safetynetAttestationService;

	/**
	 * Initializes the validator
	 *
	 * @param constraintAnnotation the annotation
	 */
	@Override
	public void initialize(ValidPublish constraintAnnotation) {
	}

	@Override
	public boolean isValid(Publish publish, ConstraintValidatorContext context) {
		boolean result = true;
		
		//check if app is authorized
		AuthorizedApp authorizedApp = authorizedAppService.findById(publish.getAppPackageName());
		if (authorizedApp == null) {
			addErrorMessage(context, "Unauthorized app");
			result = false;
		} else if (publish.getRegions().size() != publish.getRegions().stream().map(s -> authorizedApp.isRegionAllowed(s)).filter(p -> p == true).count()) {
			//check if region is allowed
			addErrorMessage(context, "Region is not allowed");
			result = false;
		}
		
		//check if device is OK - device check services are returning now true all the time !!!
		switch (publish.getPlatform()) {
			case AuthorizedAppService.IOS_DEVICE: {
				if (!deviceCheckService.isDeviceTokenValid(publish.getDeviceVerificationPayload())) {
					addErrorMessage(context, "This device is not allowed");
					result = false;
				}
				break;
			}
			case AuthorizedAppService.ANDROID_DEVICE: {
				if (!safetynetAttestationService.isAttestationValid(publish.getDeviceVerificationPayload())) {
					addErrorMessage(context, "This device is not allowed");
					result = false;
				}
				break;
			}
			default: {
				addErrorMessage(context, "Platform is not supported!");
				result = false;
				break;
			}
		}

		if (publish.getKeys().size() > maxExposureKeys) {
			addErrorMessage(context, String.format("too many exposure keys in publish:%s , max of %s is allowed!", publish.getKeys().size(), maxExposureKeys));
			result = false;
		}

		// Ensure that the uploaded keys are for a consecutive time period. No
		// overlaps and no gaps.
		// 1) Sort by interval number.
		publish.getKeys().sort(Comparator.comparing(c -> c.getIntervalNumber()));

		// 2) Walk the slice and verify no gaps/overlaps.
		// We know the slice isn't empty, seed w/ the first interval.
		Integer nextInterval = publish.getKeys().get(0).getIntervalNumber();
		for (ExposureKey key : publish.getKeys()) {
			if (key.getIntervalNumber() < nextInterval) {
				addErrorMessage(context, String.format("exposure keys have overlapping intervals"));
				result = false;
			}
			nextInterval = key.getIntervalNumber() + key.getIntervalCount();
		}
		return result;
	}

}
