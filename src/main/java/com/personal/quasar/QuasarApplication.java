package com.personal.quasar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class QuasarApplication {
	public static void main(String[] args) {
		SpringApplication.run(QuasarApplication.class, args);
	}
}
