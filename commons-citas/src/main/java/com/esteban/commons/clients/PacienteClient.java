package com.esteban.commons.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.esteban.commons.dto.PacienteResponse;

@FeignClient(name = "pacientes-msv")
public interface PacienteClient {
	
	
	//feing no aguanta patch solo los get, put y delete
	@GetMapping("/{id}")
	PacienteResponse obtenerPacientePorId(@PathVariable Long id);
	
	@GetMapping("/id-paciente/{id}")
	PacienteResponse obtenerPacientePorIdSinEstado(@PathVariable Long id);
	
	

}
