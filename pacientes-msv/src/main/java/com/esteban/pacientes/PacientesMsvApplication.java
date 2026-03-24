package com.esteban.pacientes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.esteban.pacientes",  "com.esteban.commons"})
public class PacientesMsvApplication {

	public static void main(String[] args) {
		SpringApplication.run(PacientesMsvApplication.class, args);
	}

}
