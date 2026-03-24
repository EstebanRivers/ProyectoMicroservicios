package com.esteban.citas.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.esteban.citas.dto.CitaRequest;
import com.esteban.citas.dto.CitaResponse;
import com.esteban.citas.services.CitasService;
import com.esteban.commons.controllers.CommonController;

import jakarta.validation.constraints.Positive;

@RestController 
@Validated
public class CitasController extends CommonController<CitaRequest, CitaResponse, CitasService>{

	public CitasController(CitasService service) {
		super(service);
		// TODO Auto-generated constructor stub
	}
	
	@GetMapping("/id-cita/{id}")
	public ResponseEntity<CitaResponse> obtenerCitaPorIdSinEstado(@PathVariable @Positive(message= "El ID debe ser positivo w") Long id){
		return ResponseEntity.ok(service.obtenerCitaPorIdSinEstado(id));
	}

}
