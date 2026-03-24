package com.esteban.pacientes.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esteban.commons.dto.PacienteRequest;
import com.esteban.commons.dto.PacienteResponse;
import com.esteban.commons.enums.EstadoRegistro;
import com.esteban.commons.exceptions.RecursoNoEncontrado;
import com.esteban.pacientes.entities.Paciente;
import com.esteban.pacientes.mappers.PacienteMapper;
import com.esteban.pacientes.repositories.PacienteRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PacienteServiceImpl implements PacienteServices{
	
	private final PacienteRepository pacienteRepository;
	
	private final PacienteMapper pacienteMapper;
	
	@Override
	@Transactional(readOnly = true)
	public List<PacienteResponse> listar() {
		log.info("Listado de todos los pacientes activos solicitando");
		return pacienteRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
				.map(pacienteMapper::entityToResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public PacienteResponse obtenerPorId(Long id) {
	
		return pacienteMapper.entityToResponse(obtenerPacienteOException(id));
	}

	@Override
	public PacienteResponse registrar(PacienteRequest request) {
		log.info("Registrando Paciente con el nombre: {}", request.nombre());
		
		validarEmailUnico(request.email());
		validarTelefonoUnico(request.telefono());
		
		String expediente = generacionNumExpediente(request.telefono());
		
		double imc = calculoDeIMC(request.estatura(), request.peso());
		
		Paciente paciente = pacienteRepository.save(
				pacienteMapper.requestToEntity(request, expediente, imc)); 
		
		log.info("Paciente Registrado");
		return pacienteMapper.entityToResponse(paciente);
	}

	@Override
	public PacienteResponse actualizar(PacienteRequest request, Long id) {
	    log.info("Actualizando paciente con id: {}", id);

	    Paciente paciente = obtenerPacienteOException(id);

	    validarCambiosUnicos(request, id);

	    boolean telefonoCambio = !paciente.getTelefono().equals(request.telefono());

	    if (telefonoCambio) {
	        String nuevoExpediente = generacionNumExpediente(request.telefono());
	        paciente.setNumExpediente(nuevoExpediente);

	        log.info("Expediente actualizado por cambio de teléfono");
	    }

	    double imc = calculoDeIMC(request.estatura(), request.peso());

	    paciente.setNombre(request.nombre());
	    paciente.setEmail(request.email());
	    paciente.setTelefono(request.telefono());
	    paciente.setEstatura(request.estatura());
	    paciente.setPeso(request.peso());
	    paciente.setImc(imc);

	    log.info("Paciente actualizado correctamente");

	    return pacienteMapper.entityToResponse(paciente);
	    
	    /*
	     * pacienteMapper.updateEntityFromRequest(request, paciente)
	     * 
	     * if (telefonoCambio){paciente.setTelefono(request.telefono);
	     * 
	     */
	}

	@Override
	public void eliminar(Long id) {
		Paciente paciente = obtenerPacienteOException(id);
		
		log.info("Eliminando paciente con la id: {}", id);
		paciente.setEstadoRegistro(EstadoRegistro.ELIMINADO);
		
		log.info("Paciente con el id {} ha sido marcado como eliminado", id);
	}
	
	@Override
	public PacienteResponse obtenerPacientePorIdSinEstado(Long id) {
		return pacienteMapper.entityToResponse(pacienteRepository.findById(id).orElseThrow(() -> 
		new RecursoNoEncontrado("Paciente activo no encontrado con el id: " + id)));
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private Paciente obtenerPacienteOException(Long id) {
		log.info("Buscando Paciente Activo con el id: {}", id);
		
		return pacienteRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO).orElseThrow(() -> 
		new RecursoNoEncontrado("Paciente activo no encontrado con el id: " + id));
	}
	
	private void validarEmailUnico(String email) {
        if (pacienteRepository.existsByEmailIgnoreCaseAndEstadoRegistro(email, EstadoRegistro.ACTIVO)) {
            throw new IllegalArgumentException("El email ya existe en el sistema: " + email);
        }
    }
    private void validarTelefonoUnico(String telefono) {
        if (pacienteRepository.existsByTelefonoAndEstadoRegistro(telefono, EstadoRegistro.ACTIVO)) {
            throw new IllegalArgumentException("El telefono ya existe en el sistema: " + telefono);
        }
    }
    private void validarCambiosUnicos(PacienteRequest request, Long id) {
        if (pacienteRepository.existsByEmailIgnoreCaseAndIdNotAndEstadoRegistro(request.email(), id, EstadoRegistro.ACTIVO)) {
            throw new IllegalArgumentException("El email ya existe en el sistema: " + request.email());
        }
        if (pacienteRepository.existsByTelefonoAndIdNotAndEstadoRegistro(request.telefono(), id, EstadoRegistro.ACTIVO)) {
            throw new IllegalArgumentException("El telefono ya existe en el sistema:  " + request.telefono());
        }
    }
	
    private double calculoDeIMC(Double estatura, Double peso) {
        if (estatura == null || estatura == 0) {
            throw new IllegalArgumentException("La estatura no puede ser 0");
        }
        double imc = peso / Math.pow(estatura, 2);

        double scale = Math.pow(10, 2);
        return Math.round(imc * scale) / scale;
    }
	
	private String generacionNumExpediente(String telefono) {
	
	    String expediente = telefono.chars()
	            .mapToObj(c -> String.valueOf((char) c) + "X")
	            .collect(Collectors.joining());
	        
	        log.debug("Expediente generado: {} → {}", telefono, expediente);
	        
	        return expediente;
	}

	

}
