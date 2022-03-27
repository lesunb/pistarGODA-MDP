package br.unb.cic.integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("br.unb.cic"))
	            .paths(PathSelectors.any()).build().apiInfo(this.metaInfo());
	}

	private ApiInfo metaInfo() {
		return new ApiInfoBuilder().title("Projeto PiStar/GODA")
				.description("GODA - Goal Oriented Dependability Analisys")
				.contact(new Contact("Universidade de Brasília", "Departamento de Ciência da Computação", "cic@unb.br"))
				.version("3.0.0").build();

	}

}

