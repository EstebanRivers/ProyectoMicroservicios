package com.esteban.pacientes.services;

import com.esteban.commons.dto.PacienteRequest;
import com.esteban.commons.dto.PacienteResponse;
import com.esteban.commons.services.CrudService;

public interface PacienteServices extends CrudService<PacienteRequest, PacienteResponse>{
	
	PacienteResponse obtenerPacientePorIdSinEstado(Long id);

}
