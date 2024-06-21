package br.unb.cic.integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static int server() {
        SpringApplication.run(Application.class);
        return 0;
    }
}