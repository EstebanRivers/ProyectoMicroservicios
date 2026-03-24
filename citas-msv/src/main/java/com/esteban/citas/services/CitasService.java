package com.esteban.citas.services;

import com.esteban.citas.dto.CitaRequest;
import com.esteban.citas.dto.CitaResponse;
import com.esteban.commons.services.CrudService;

public interface CitasService extends CrudService<CitaRequest, CitaResponse>{

	CitaResponse obtenerCitaPorIdSinEstado(Long id);
	
}
