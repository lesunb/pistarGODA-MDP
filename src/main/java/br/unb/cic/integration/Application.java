package br.unb.cic.integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import br.unb.cic.goda.exception.ResponseExceptionHandler;

@SpringBootApplication
@Import(ResponseExceptionHandler.class)
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}