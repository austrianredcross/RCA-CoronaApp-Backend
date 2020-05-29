package at.roteskreuz.covidapp.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


/**
 * Filter that checks the authorization header
 * 
 * @author Zoltán Puskai
 */
@Component
public class AuthorizationHeaderFilter extends OncePerRequestFilter {
	/**
	 * The authorization key in the HTTP header
	 */
	private static final String API_PATH = "/api/";

	@Value("${http.request.api.key:key}")
	private String apiKey;

	@Value("${http.request.api.value:value}")
	private String apiValue;
	
	
	/**
	 * If the authorization key is not present or the value is not valid, it will throw an exception
	 * 
	 * @param request HTTP request
	 * @param response HTTP response
	 * @param filterChain filterChain
	 * @throws ServletException exception  that could be thrown by the chain
	 * @throws IOException exception  that could be thrown by the chain
	 */	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		boolean isForbidden = false;
		if (request.getServletPath().startsWith(API_PATH)) {
		String authorizationValue = request.getHeader(apiKey);
			if (authorizationValue == null || !authorizationValue.equals(apiValue)) {
				response.sendError(403, "Authorization required");
				isForbidden = true;
			}
		}
		if (!isForbidden) {
			filterChain.doFilter(request, response);
		}
	}

}
