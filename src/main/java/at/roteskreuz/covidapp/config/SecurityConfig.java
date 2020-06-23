package at.roteskreuz.covidapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Class for configuring security
 *
 * @author Zoltán Puskai
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${application.api.version}")
	private String appVersion;

	/**
	 * Configures the http security
	 *
	 * 
	 * @param http http security to be configured
	 * @throws java.lang.Exception authentication cannot be configured
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.authorizeRequests()
				.antMatchers("/api/v" + appVersion + "/*").permitAll()
				.anyRequest().fullyAuthenticated()
				.and()
				.httpBasic()
				.and()
				.formLogin()
				.and()
				.logout()
				.permitAll();
	}

}
