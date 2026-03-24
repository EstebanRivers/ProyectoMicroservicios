package com.esteban.pacientes.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.esteban.commons.controllers.CommonController;
import com.esteban.commons.dto.PacienteRequest;
import com.esteban.commons.dto.PacienteResponse;
import com.esteban.pacientes.services.PacienteServices;

import jakarta.validation.constraints.Positive;

@RestController 
@Validated
public class PacienteController extends CommonController<PacienteRequest, PacienteResponse, PacienteServices>{

	public PacienteController(PacienteServices service) {
		super(service);
		// TODO Auto-generated constructor stub
	}
	
	@GetMapping("/id-paciente/{id}")
	public ResponseEntity<PacienteResponse> obtenerPacientePorIdSinEstado(@PathVariable @Positive(message= "El ID debe ser positivo w") Long id){
		return ResponseEntity.ok(service.obtenerPacientePorIdSinEstado(id));
	}

}
