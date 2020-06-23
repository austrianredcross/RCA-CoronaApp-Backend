package at.roteskreuz.covidapp.config;

import com.fasterxml.classmate.TypeResolver;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configuration class for enabling Swagger 2 documentation
 *
 * @author Zoltán Puskai
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Value("${application.api.version}")
	private int appVersion;

	@Value("${http.request.api.key:key}")
	private String apiKey;
	
	/**
	 * Configures Swagger 2 for generating API documentation
	 *
	 * @param typeResolver typeResolver to be configured
	 * @return Docket Swagger docket
	 */			
	@Bean
	public Docket api(TypeResolver typeResolver) {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("at.roteskreuz.covidapp.api"))
				.paths(PathSelectors.ant("/api/v" + appVersion + "/*"))
				.build().apiInfo(apiInfo())
				.alternateTypeRules(AlternateTypeRules.newRule(
						typeResolver.resolve(List.class, LocalDateTime.class),
						typeResolver.resolve(List.class, Date.class), Ordered.HIGHEST_PRECEDENCE))
				.securitySchemes(Collections.singletonList(new ApiKey(apiKey, apiKey, "header")));
	}

	private ApiInfo apiInfo() {
		return new ApiInfo(
				"CoronaApp API",
				"CoronaApp API description.",
				"v" + appVersion,
				"",
				null,"", "", Collections.emptyList());
	}
}
