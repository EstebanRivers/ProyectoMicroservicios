package com.esteban.commons;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class CommonsCitasApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommonsCitasApplication.class, args);
	}

}
