package at.roteskreuz.covidapp.service;

import at.roteskreuz.covidapp.domain.AuthorizedApp;
import at.roteskreuz.covidapp.repository.AuthorizedAppRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service class to manage authorized apps 
 * 
 * @author Zoltán Puskai
 */
@Service
@RequiredArgsConstructor
public class AuthorizedAppService {
	
	public static final String IOS_DEVICE = "ios";
	public static final String ANDROID_DEVICE = "android";		
	
	private final AuthorizedAppRepository authorizedAppRepository;

	/**
	 * Finds an authorized app by id
	 * @param id
	 * @return 
	 */
	public AuthorizedApp findById(String id) {
		return authorizedAppRepository.findById(id).orElse(null);
	}
}
