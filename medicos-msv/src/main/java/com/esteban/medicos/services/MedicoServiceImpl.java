package com.esteban.medicos.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esteban.commons.dto.MedicoRequest;
import com.esteban.commons.dto.MedicoResponse;
import com.esteban.commons.enums.DisponibilidadMedico;
import com.esteban.commons.enums.EspecialidadMedico;
import com.esteban.commons.enums.EstadoRegistro;
import com.esteban.commons.exceptions.RecursoNoEncontrado;
import com.esteban.medicos.entities.Medico;
import com.esteban.medicos.mappers.MedicoMapper;
import com.esteban.medicos.repositories.MedicoRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class MedicoServiceImpl implements MedicoService{
	
	private final MedicoRepository medicoRepository;
	
	private final MedicoMapper medicoMapper;
	
	@Override
	public List<MedicoResponse> listar() {
		log.info("Listado de todos los Medicos activos solicitando");
		return medicoRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
				.map(medicoMapper::entityToResponse).toList();
	}

	@Override
	public MedicoResponse obtenerPorId(Long id) {
		
		return medicoMapper.entityToResponse(obtenerMedicoOException(id));
	}
	
	@Override
	public MedicoResponse obtenerMedicoPorIdSinEstado(Long id) {
		return medicoMapper.entityToResponse(medicoRepository.findById(id).orElseThrow(() -> 
		new RecursoNoEncontrado("Medico sin estado no encontrado con el id: " + id)));
	}

	@Override
	public MedicoResponse registrar(MedicoRequest request) {
		
		
		validarEmailUnico(request.email());
		validarTelefonoUnico(request.telefono());
		validarCedulaUnica(request.cedulaProfesional());
		
		Medico medico = medicoMapper.requestToEntity(request);
		
		medico.setEspecialidad(EspecialidadMedico.fromCodigo(request.idEspecialidad()));
		medico.setDisponibilidad(DisponibilidadMedico.DISPONIBLE);
		
		medicoRepository.save(medico);
		
		return medicoMapper.entityToResponse(medico);
	}

	@Override
	public MedicoResponse actualizar(MedicoRequest request, Long id) {
		Medico medico = obtenerMedicoOException(id);
		
		validarCambiosUnicos(request, id);
		
		medicoMapper.updateEntityFromRequest(request, medico);
		
		medico.setEspecialidad(EspecialidadMedico.fromCodigo(request.idEspecialidad()));
		
		return medicoMapper.entityToResponse(medico);
	}

	@Override
	public void eliminar(Long id) {
		Medico medico = obtenerMedicoOException(id);
		
		medico.setEstadoRegistro(EstadoRegistro.ELIMINADO);
		
	}
	
	private Medico obtenerMedicoOException(Long id) {
		log.info("Buscando Paciente Activo con el id: {}", id);
		
		return medicoRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO).orElseThrow(() -> 
		new RecursoNoEncontrado("Paciente activo no encontrado con el id: " + id));
	}
	
	private void validarEmailUnico(String email) {
        if (medicoRepository.existsByEmailIgnoreCaseAndEstadoRegistro(email, EstadoRegistro.ACTIVO)) {
            throw new IllegalArgumentException("El email ya existe en el sistema: " + email);
        }
    }
    private void validarTelefonoUnico(String telefono) {
        if (medicoRepository.existsByTelefonoAndEstadoRegistro(telefono, EstadoRegistro.ACTIVO)) {
            throw new IllegalArgumentException("El telefono ya existe en el sistema: " + telefono);
        }
    }
    private void validarCedulaUnica(String cedula) {
    	if (medicoRepository.existsByCedulaProfecionalAndEstadoRegistro(cedula, EstadoRegistro.ACTIVO)) {
    		throw new IllegalArgumentException("Esta cedula ya esta registrada en el sistema: " + cedula);
    	}
    }
    
    private void validarCambiosUnicos(MedicoRequest request, Long id) {
        if (medicoRepository.existsByEmailIgnoreCaseAndIdNotAndEstadoRegistro(request.email(), id, EstadoRegistro.ACTIVO)) {
            throw new IllegalArgumentException("El email ya existe en el sistema: " + request.email());
        }
        if (medicoRepository.existsByTelefonoAndIdNotAndEstadoRegistro(request.telefono(), id, EstadoRegistro.ACTIVO)) {
            throw new IllegalArgumentException("El telefono ya existe en el sistema:  " + request.telefono());
        }
        if (medicoRepository.existsByCedulaProfecionalAndIdNotAndEstadoRegistro(request.cedulaProfesional(), id, EstadoRegistro.ACTIVO)) {
        	throw new IllegalArgumentException("El telefono ya existe en el sistema:  " + request.cedulaProfesional());
        }
    }	

}
