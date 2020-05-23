package at.roteskreuz.covidapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Service for flushing the client configuration cache periodically
 * client.configuration.cache.ttl -  the number of milliseconds between 2 calls can be configured in the application.properties
 * 
 * @author Zoltán Puskai
 * @since 0.0.1-SNAPSHOT
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientConfigurationCacheService {
	
	private final ClientConfigurationService service;
	
	/**
	 * Flushes the client configuration cache
	 */
	@Scheduled(fixedRateString ="${client.configuration.cache.ttl}", initialDelayString="${client.configuration.cache.ttl}")
	public void flushCache() {
		log.trace("Flushing the client configuration cache");
		service.flushCache();
	}

}
