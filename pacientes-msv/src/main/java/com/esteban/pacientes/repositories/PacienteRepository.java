package com.esteban.pacientes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.esteban.pacientes.entities.Paciente;
import java.util.List;
import java.util.Optional;

import com.esteban.commons.enums.EstadoRegistro;


@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long>{
	
	boolean existsByEmailIgnoreCaseAndEstadoRegistro(String email, EstadoRegistro estadoRegistro);

    boolean existsByTelefonoAndEstadoRegistro(String telefono, EstadoRegistro estadoRegistro);
    
    boolean existsByTelefonoAndIdNotAndEstadoRegistro(String telefono, Long id, EstadoRegistro estadoRegistro);

    boolean existsByEmailIgnoreCaseAndIdNotAndEstadoRegistro(String email, Long id, EstadoRegistro estadoRegistro);
	
	List<Paciente> findByEstadoRegistro(EstadoRegistro estadoRegistro);
	
	Optional<Paciente> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);

}
