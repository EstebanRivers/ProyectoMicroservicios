package com.esteban.citas.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esteban.citas.dto.CitaRequest;
import com.esteban.citas.dto.CitaResponse;
import com.esteban.citas.entities.Cita;
import com.esteban.citas.enums.EstadoCita;
import com.esteban.citas.mappers.CitasMapper;
import com.esteban.citas.repositories.CitaRepository;
import com.esteban.commons.clients.MedicoClient;
import com.esteban.commons.clients.PacienteClient;
import com.esteban.commons.dto.MedicoResponse;
import com.esteban.commons.dto.PacienteResponse;
import com.esteban.commons.enums.EstadoRegistro;
import com.esteban.commons.exceptions.RecursoNoEncontrado;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CitasServiceImpl implements CitasService{
	
	private final CitaRepository citaRepository;
	
	private final CitasMapper citaMapper;
	
	private final PacienteClient pacienteClient;
	
	private final MedicoClient medicoClient;
	
	@Override
	@Transactional(readOnly = true)
	public List<CitaResponse> listar() {
		
		return citaRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
				.map(cita -> citaMapper.entityToResponse(cita, 
						obtenerPacienteResponseSinEstado(cita.getIdPaciente()), 
						obtenerMedicoResponseSinEstado(cita.getIdMedico())))
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public CitaResponse obtenerPorId(Long id) {
		Cita cita = obtenerCitaOException(id);
		return citaMapper.entityToResponse(cita, 
				obtenerPacienteResponseSinEstado(cita.getIdPaciente()), 
				obtenerMedicoResponseSinEstado(cita.getIdMedico()) );
	}
	
	@Override
	@Transactional(readOnly = true)
	public CitaResponse obtenerCitaPorIdSinEstado(Long id) {
		
		Cita cita = citaRepository.findById(id).orElseThrow(() ->
		new RecursoNoEncontrado("Cita sin estado no encontrada con ID: " + id));
		
		return citaMapper.entityToResponse(cita, 
				obtenerPacienteResponseSinEstado(cita.getIdPaciente()), 
				obtenerMedicoResponseSinEstado(cita.getIdMedico()));
	}

	@Override
	public CitaResponse registrar(CitaRequest request) {
		
		PacienteResponse paciente = obtenerPacienteResponse(request.idPaciente());
		MedicoResponse medico = obtenerMedicoResponse(request.idMedico());
		
		Cita cita = citaRepository.save(citaMapper.requestToEntity(request));
		
		return citaMapper.entityToResponse(cita, paciente, medico);
	}

	@Override
	public CitaResponse actualizar(CitaRequest request, Long id) {
		Cita cita = obtenerCitaOException(id);
		
		PacienteResponse paciente = obtenerPacienteResponse(request.idPaciente());
		MedicoResponse medico = obtenerMedicoResponse(request.idMedico());

		EstadoCita estadoNuevo = EstadoCita.fromCodigo(request.idEstadoCita());
		
		citaMapper.updateEntityFromRequest(request, cita, estadoNuevo);
		
		return citaMapper.entityToResponse(cita, paciente, medico);
	}

	@Override
	public void eliminar(Long id) {
		Cita cita = obtenerCitaOException(id);
		
		validarEstadoCitaAlEliminar(cita);
		
		//Cambiar disponibilidad del medico a disponible solo si esta en pendiente
		
		cita.setEstadoRegistro(EstadoRegistro.ELIMINADO);
		
	}
	
	
	private Cita obtenerCitaOException(long id) {
		return citaRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO).orElseThrow(()->
		new RecursoNoEncontrado("Cita no encontrada con el id: " + id));
	}
	
	private void validarEstadoCitaAlEliminar(Cita cita) {
		if (cita.getEstadoCita() == EstadoCita.CONFIRMADA || cita.getEstadoCita() == EstadoCita.EN_CURSO) {
			throw new IllegalStateException("No se puede eliminar una cita " + 
					EstadoCita.EN_CURSO.getDescripcion() + " o " + EstadoCita.CONFIRMADA.getDescripcion());
		}
	}
	
	private PacienteResponse obtenerPacienteResponse(Long idPaciente) {
		return pacienteClient.obtenerPacientePorId(idPaciente);
	}
	
	private PacienteResponse obtenerPacienteResponseSinEstado(Long idPaciente) {
		return pacienteClient.obtenerPacientePorIdSinEstado(idPaciente);
	}
	
	private MedicoResponse obtenerMedicoResponse(Long idMedico) {
		return medicoClient.obtenerMedicoPorId(idMedico);
	}
	
	private MedicoResponse obtenerMedicoResponseSinEstado(Long idMedico) {
		return medicoClient.obtenerMedicoPorIdSinEstado(idMedico);
	}
	
	

}
