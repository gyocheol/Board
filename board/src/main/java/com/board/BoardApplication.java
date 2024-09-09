package com.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BoardApplication {

	public static void main(String[] args) {
		System.setProperty("spring.config.name", "application, application-db, application-login, application-redis");
		SpringApplication.run(BoardApplication.class, args);
	}

}
