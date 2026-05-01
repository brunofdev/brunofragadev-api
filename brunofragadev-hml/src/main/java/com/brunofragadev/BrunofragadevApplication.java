package com.brunofragadev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BrunofragadevApplication {

	private static final Logger log = LoggerFactory.getLogger(BrunofragadevApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BrunofragadevApplication.class, args);
		log.info("Servidor inicializado com sucesso!");
	}
}
