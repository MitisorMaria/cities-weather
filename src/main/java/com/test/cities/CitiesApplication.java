package com.test.cities;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
public class CitiesApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(CitiesApplication.class, args);
	}

}