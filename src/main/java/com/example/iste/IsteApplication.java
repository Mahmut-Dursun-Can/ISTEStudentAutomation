package com.example.iste;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class IsteApplication {

	public static void main(String[] args) {
		SpringApplication.run(IsteApplication.class, args);
	}

}
