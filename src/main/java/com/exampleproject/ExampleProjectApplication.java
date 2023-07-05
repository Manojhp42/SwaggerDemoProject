package com.exampleproject;

import java.util.Base64;
import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;



import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class ExampleProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExampleProjectApplication.class, args);
		 String clientId = "myClientId";
		    String clientSecret = "myClientSecret";
		    String encodedCredentials = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
		    System.out.println("Encoded credentials: " + encodedCredentials);
	}

	@Bean
	public Docket swaggerConfiguration() {

		return new Docket(DocumentationType.SWAGGER_2).groupName("V1").apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.exampleproject.controller"))
				.paths(PathSelectors.any()).build();
	}

	public ApiInfo apiInfo() {
	return new ApiInfo("EMPLOYEES", "This API is related to employee", "V1.0", "XYZ",
				new Contact("Manoj", "xyz", "xyz@gmail.com"), "none", "none", Collections.emptyList());
		
	}

	
	@Bean
	WebClient webClient(WebClient.Builder builder) {
		return  builder
				.build();
	}
}
