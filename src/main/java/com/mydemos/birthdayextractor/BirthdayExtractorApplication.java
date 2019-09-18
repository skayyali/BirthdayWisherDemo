package com.mydemos.birthdayextractor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.mydemos.birthdayextractor.*")
public class BirthdayExtractorApplication {

	public static void main(String[] args) {
		SpringApplication.run(BirthdayExtractorApplication.class, args);
	}

}
