package at.roteskreuz.covidapp.service;

import at.roteskreuz.covidapp.domain.AuthorizedApp;
import at.roteskreuz.covidapp.exception.AuthorizedAppNotFoundException;
import at.roteskreuz.covidapp.repository.AuthorizedAppRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author zolika
 */

@Service
@RequiredArgsConstructor
public class AuthorizedAppService {
	
	public static final String IOS_DEVICE = "ios";
	public static final String ANDROID_DEVICE = "android";		
	
	private final AuthorizedAppRepository authorizedAppRepository;

	public AuthorizedApp findById(String id) throws AuthorizedAppNotFoundException {
		return authorizedAppRepository.findById(id).orElseThrow(() -> {return new AuthorizedAppNotFoundException();});
	}
}
