package at.roteskreuz.covidapp.service;

import at.roteskreuz.covidapp.domain.AuthorizedApp;
import at.roteskreuz.covidapp.domain.Exposure;
import at.roteskreuz.covidapp.exception.AuthorizedAppNotFoundException;
import at.roteskreuz.covidapp.exception.DeviceCheckFailedException;
import at.roteskreuz.covidapp.exception.PlatformNotSupportedException;
import at.roteskreuz.covidapp.exception.RegionNotAllowedException;
import at.roteskreuz.covidapp.exception.SafetynetAttestationFailedException;
import at.roteskreuz.covidapp.model.ApiResponse;
import at.roteskreuz.covidapp.model.Publish;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author zolika
 */
@Service
@RequiredArgsConstructor
public class PublishService {
	
	
	private final AuthorizedAppService authorizedAppService;
	private final DeviceCheckService deviceCheckService;
	private final SafetynetAttestationService safetynetAttestationService;
	private final ExposureService exposureService;

	public ApiResponse publish(Publish publish) throws AuthorizedAppNotFoundException, RegionNotAllowedException, PlatformNotSupportedException, DeviceCheckFailedException, SafetynetAttestationFailedException {
		AuthorizedApp authorizedApp = authorizedAppService.findById(publish.getAppPackageName());
		if (publish.getRegions().size() != publish.getRegions().stream().map(s-> authorizedApp.isRegionAllowed(s)).filter(p -> p == true).count()) {
			throw new RegionNotAllowedException();
		}
		switch (publish.getPlatform()) {
			case AuthorizedAppService.IOS_DEVICE : {
				if (!deviceCheckService.isDeviceTokenValid(publish.getDeviceVerificationPayload())) {
					throw new DeviceCheckFailedException();
				}
				break;
			}
			case AuthorizedAppService.ANDROID_DEVICE : {
				if (!safetynetAttestationService.isAttestationValid(publish.getDeviceVerificationPayload())) {
					throw new SafetynetAttestationFailedException();
				}
				break;
			}
			default: throw new PlatformNotSupportedException();
		}
		LocalDateTime now = LocalDateTime.now();
		
		String appPackageName = publish.getAppPackageName();
		List<String> regions = publish.getRegions();
		
		publish.getRegions().replaceAll(String::toUpperCase);
		
		exposureService.saveAll(
			publish.getKeys()
				.stream()
				.map(k-> 
					new Exposure( 
							k.getBinKey(), 
							k.getTransmissionRisk(), 
							publish.getAppPackageName(),
							publish.getRegions(), 
							k.getIntervalNumber(), 
							k.getIntervalCount(), 
							now , 
							true, 
							null))
				.collect(Collectors.toList()));

		return new ApiResponse(200, "OK", "", 0, false);
	}

}
