package at.roteskreuz.covidapp.service;

import at.roteskreuz.covidapp.domain.ClientConfiguration;
import at.roteskreuz.covidapp.repository.ClientConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Service for retrieving the client configuration
 * 
 * @author Zoltán Puskai
 * @since 0.0.1-SNAPSHOT
 */

@Service
@RequiredArgsConstructor
public class ClientConfigurationService {

	private final ClientConfigurationRepository repository;
	
	/**
	 * Returns (and caches) the current client configuration
	 * 
	 * @return client configuration 
	 */
	@Cacheable(cacheNames = "configuration")
	public ClientConfiguration getConfiuration() {
		return repository.findById(1L).orElse(null);
	}

	
	/**
	 * Flushes the client configuration cache
	 */
	@CacheEvict(allEntries = true, cacheNames = "configuration")
	public void flushCache() {
	}
}
