package com.esteban.medicos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.esteban.medicos.entities.Medico;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long>{

}
