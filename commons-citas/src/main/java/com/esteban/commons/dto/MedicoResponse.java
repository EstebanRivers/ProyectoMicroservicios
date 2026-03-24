package com.esteban.commons.dto;

public record MedicoResponse(
		
		Long id,
		String nombre,
		Short edad,
		String email,
		String telefono,
		String cedulaProfecional,
		String especialidad,
		String disponibilidad
		
		) 
{}
