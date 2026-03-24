package com.esteban.medicos.mappers;

import org.springframework.stereotype.Component;

import com.esteban.commons.dto.MedicoRequest;
import com.esteban.commons.dto.MedicoResponse;
import com.esteban.commons.enums.EstadoRegistro;
import com.esteban.commons.mappers.CommonMapper;
import com.esteban.medicos.entities.Medico;

@Component
public class MedicoMapper implements CommonMapper<MedicoRequest, MedicoResponse, Medico>{

	@Override
	public Medico requestToEntity(MedicoRequest request) {
		if (request == null) return null;

        return Medico.builder()
                .nombre(request.nombre())
                .apellidoPaterno(request.apellidoPaterno())
                .apellidoMaterno(request.apellidoMaterno())
                .edad(request.edad())
                .email(request.email())
                .telefono(request.telefono())
                .cedulaProfecional(request.cedulaProfesional())
                .estadoRegistro(EstadoRegistro.ACTIVO)
                .build();
	}

	@Override
	public MedicoResponse entityToResponse(Medico entity) {
		if(entity == null) return null;
		return new MedicoResponse(
				entity.getId(),
				String.join(" ", entity.getNombre(),
						entity.getApellidoPaterno(),
						entity.getApellidoMaterno()),
				entity.getEdad(),
				entity.getEmail(),
				entity.getTelefono(),
				entity.getCedulaProfecional(),
				entity.getEspecialidad().getDescripcion(),
				entity.getDisponibilidad().getDescripcion()
		);
	}

	@Override
	public Medico updateEntityFromRequest(MedicoRequest request, Medico entity) {
		if (request == null || entity == null) return null;

        entity.setNombre(request.nombre());
        entity.setApellidoPaterno(request.apellidoPaterno());
        entity.setApellidoMaterno(request.apellidoMaterno());
        entity.setEdad(request.edad());
        entity.setEmail(request.email());
        entity.setTelefono(request.telefono());
        entity.setCedulaProfecional(request.cedulaProfesional());

        return entity;
	}

}
