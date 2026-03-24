package com.esteban.medicos.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.esteban.commons.enums.EstadoRegistro;
import com.esteban.medicos.entities.Medico;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long>{
	
	List<Medico> findByEstadoRegistro(EstadoRegistro estadoRegistro);
	
	Optional<Medico> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);
	
	boolean existsByEmailIgnoreCaseAndEstadoRegistro(String email, EstadoRegistro estadoRegistro);

    boolean existsByTelefonoAndEstadoRegistro(String telefono, EstadoRegistro estadoRegistro);
    
    boolean existsByTelefonoAndIdNotAndEstadoRegistro(String telefono, Long id, EstadoRegistro estadoRegistro);

    boolean existsByEmailIgnoreCaseAndIdNotAndEstadoRegistro(String email, Long id, EstadoRegistro estadoRegistro);
    
    boolean existsByCedulaProfecionalAndEstadoRegistro(String cedulaProfecional, EstadoRegistro estadoRegistro);
    
    boolean existsByCedulaProfecionalAndIdNotAndEstadoRegistro(String cedulaProfecional, Long id, EstadoRegistro estadoRegistro);

}
