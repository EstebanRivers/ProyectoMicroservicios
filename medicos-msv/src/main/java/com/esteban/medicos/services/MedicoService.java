package com.esteban.medicos.services;

import com.esteban.commons.dto.MedicoRequest;
import com.esteban.commons.dto.MedicoResponse;
import com.esteban.commons.services.CrudService;

public interface MedicoService extends CrudService<MedicoRequest, MedicoResponse>{
	
	MedicoResponse obtenerMedicoPorIdSinEstado(Long id);

}
