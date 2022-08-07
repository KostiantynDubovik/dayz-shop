package com.dayz.shop.utils;


import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurerAdapter implements EnvironmentAware {
	@Bean
	public Docket swaggerSpringfoxDocket() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
				.genericModelSubstitutes(ResponseEntity.class).forCodeGeneration(true)
				.genericModelSubstitutes(ResponseEntity.class)
				.directModelSubstitute(LocalDate.class, String.class)
				.directModelSubstitute(LocalDateTime.class, Date.class)
				.directModelSubstitute(ZonedDateTime.class, Date.class).select()
				.paths(regex("/api/.*")).build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("DayZ-shop API")
				.description("DayZ-shop API reference for developers")
				.termsOfServiceUrl("http://alcatraz.dayz-shop.com")
				.contact(new Contact("Kostiantyn Dubovik", "https://www.instagram.com/kostiantyndubovik","kostiantyn.dubovik@gmail.com")).license("DayZ-shop License")
				.licenseUrl("kostiantyn.dubovik@gmail.com").version("1.0").build();
	}


	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html")
				.addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	@Override
	public void setEnvironment(Environment environment) {

	}
}
