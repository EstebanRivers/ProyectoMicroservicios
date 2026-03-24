package com.esteban.citas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esteban.citas.entities.Cita;
import java.util.List;
import java.util.Optional;

import com.esteban.commons.enums.EstadoRegistro;


public interface CitaRepository extends JpaRepository<Cita, Long>{
	
	List<Cita> findByEstadoRegistro(EstadoRegistro estadoRegistro);
	
	Optional<Cita> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);

}
