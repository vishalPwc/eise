package com.aafes.settlement.configuration;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket postsApi() {
		Docket docket = new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build()
				.securitySchemes(Lists.newArrayList(apiKey()))
				.securityContexts(Lists.newArrayList(securityContext()));

		docket.groupName("public-api")
				.apiInfo(apiInfo()).select().paths(postPaths()).build();

		docket.globalResponseMessage(
				RequestMethod.GET, ImmutableList.of(
						new ResponseMessageBuilder()
								.code(400)
								.message("Bad Request")
								.responseModel(new ModelRef("string")).build(),
						new ResponseMessageBuilder()
								.code(500)
								.message("Internal Server Error")
								.responseModel(new ModelRef("string")).build()
				)
		);

		return docket;
	}

	@Bean
	SecurityContext securityContext() {
		return SecurityContext.builder()
				.securityReferences(defaultAuth())
				.forPaths(PathSelectors.any())
				.build();
	}

	List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope(
				"global", "accessEverything"
		);
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Lists.newArrayList(
				new SecurityReference("JWT", authorizationScopes)
		);
	}

	private ApiKey apiKey() {
		return new ApiKey("JWT", "Authorization", "header");
	}

	/*
	 * private ApiKey apiKey() { return new ApiKey( "JWT", "Authorization",
	 * "header" ); }
	 */

	// public static final String DEFAULT_INCLUDE_PATTERN = "/omsservice/.*";

	/*
	 * private SecurityContext securityContext() { return
	 * SecurityContext.builder() .securityReferences(defaultAuth())
	 * //.forPaths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERN))
	 * .forPaths(PathSelectors.regex("/api/auths/.*")) //to add jwt add path
	 * .build(); }
	 */

	/*
	 * List<SecurityReference> defaultAuth() { AuthorizationScope
	 * authorizationScope = new AuthorizationScope("global",
	 * "accessEverything"); AuthorizationScope[] authorizationScopes = new
	 * AuthorizationScope[1]; authorizationScopes[0] = authorizationScope;
	 * return Lists.newArrayList( new SecurityReference("JWT",
	 * authorizationScopes)); }
	 */

	@SuppressWarnings("unchecked")
	private Predicate<String> postPaths() {
		return or(
				regex("/api/auth/.*"), regex("/omsservice/.*"), regex(
						"/api/.*"
				), regex("/.*")
		);
	}

	@SuppressWarnings("deprecation")
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("swagger API")
				.description("swagger API reference for developers")
				.termsOfServiceUrl("http://swagger.com")
				.contact("swagger@gmail.com").license("Swagger License")
				.licenseUrl("swagger@gmail.com").version("1.0").build();
	}

}
