package com.esteban.medicos.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.esteban.commons.controllers.CommonController;
import com.esteban.commons.dto.MedicoRequest;
import com.esteban.commons.dto.MedicoResponse;
import com.esteban.medicos.services.MedicoService;

import jakarta.validation.constraints.Positive;

@RestController 
@Validated
public class MedicoController extends CommonController<MedicoRequest, MedicoResponse, MedicoService>{

	public MedicoController(MedicoService service) {
		super(service);
		// TODO Auto-generated constructor stub
	}
	
	@GetMapping("/id-medico/{id}")
	public ResponseEntity<MedicoResponse> obtenerMedicoPorIdSinEstado(@PathVariable @Positive(message= "El ID debe ser positivo w") Long id){
		return ResponseEntity.ok(service.obtenerMedicoPorIdSinEstado(id));
	}

}
