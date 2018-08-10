package com.sample.manish.sleuthtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SleuthtestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SleuthtestApplication.class, args);
	}
}
