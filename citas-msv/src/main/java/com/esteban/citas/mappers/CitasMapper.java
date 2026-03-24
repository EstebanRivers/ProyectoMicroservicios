package com.esteban.citas.mappers;

import org.springframework.stereotype.Component;

import com.esteban.citas.dto.CitaRequest;
import com.esteban.citas.dto.CitaResponse;
import com.esteban.citas.entities.Cita;
import com.esteban.citas.enums.EstadoCita;
import com.esteban.commons.dto.DatosMedico;
import com.esteban.commons.dto.DatosPaciente;
import com.esteban.commons.dto.MedicoResponse;
import com.esteban.commons.dto.PacienteResponse;
import com.esteban.commons.enums.EstadoRegistro;
import com.esteban.commons.mappers.CommonMapper;

@Component
public class CitasMapper implements CommonMapper<CitaRequest, CitaResponse, Cita>{

	@Override
	public Cita requestToEntity(CitaRequest request) {
		if(request == null) return null;
		
		return Cita.builder()
				.idPaciente(request.idPaciente())
				.idMedico(request.idMedico())
				.fechaCita(request.fechaCita())
				.sintomas(request.sintomas())
				.estadoCita(EstadoCita.PENDIENTE)
				.estadoRegistro(EstadoRegistro.ACTIVO)
				.build();
	}

	@Override
	public CitaResponse entityToResponse(Cita entity) {
		if(entity == null) return null;
		
		return new CitaResponse(
				entity.getId(), 
				null, 
				null,
				entity.getFechaCita(),
				entity.getSintomas(), 
				entity.getEstadoCita().getDescripcion());
	}
	public CitaResponse entityToResponse(Cita entity, PacienteResponse paciente) {
		if(entity == null) return null;
		
		return new CitaResponse(
				entity.getId(), 
				pacienteResponseToDatosPaciente(paciente), 
				null,
				entity.getFechaCita(),
				entity.getSintomas(), 
				entity.getEstadoCita().getDescripcion());
	}
	public CitaResponse entityToResponse(Cita entity, PacienteResponse paciente, MedicoResponse medico) {
		if(entity == null) return null;
		
		return new CitaResponse(
				entity.getId(), 
				pacienteResponseToDatosPaciente(paciente), 
				medicoResponseToDatosMedicos(medico),
				entity.getFechaCita(),
				entity.getSintomas(), 
				entity.getEstadoCita().getDescripcion());
	}

	@Override
	public Cita updateEntityFromRequest(CitaRequest request, Cita entity) {
		if(entity == null) return null;
		
		entity.setIdPaciente(request.idPaciente());
		entity.setIdMedico(request.idMedico());
		entity.setFechaCita(request.fechaCita());
		entity.setSintomas(request.sintomas());
		
		return entity;
	}
	public Cita updateEntityFromRequest(CitaRequest request, Cita entity, EstadoCita estadoCita) {
		if(entity == null) return null;
		
		updateEntityFromRequest(request, entity);
		entity.setEstadoCita(estadoCita);
		
		return entity;
	}
	
	private DatosPaciente pacienteResponseToDatosPaciente(PacienteResponse paciente) {
		if (paciente == null) return null;
		
		return new DatosPaciente(
				paciente.nombre(),
				paciente.numExpediente(),
				paciente.edad() + "anos",
				paciente.peso() + "kg.",
				paciente.estatura() + "m",
				paciente.imc() + "",
				paciente.telefono());
	}
	
	private DatosMedico medicoResponseToDatosMedicos(MedicoResponse medico) {
		if (medico == null) return null;
		
		return new DatosMedico(
				medico.nombre(),
				medico.cedulaProfecional(),
				medico.especialidad());
				
	}
	
}
