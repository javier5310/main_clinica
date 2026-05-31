package com.demo.persistencia.demopersistencia.repositorio;

import com.demo.persistencia.demopersistencia.entidades.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
    List<Medico> findByDisponibleTrue();
    List<Medico> findByEspecialidadAndDisponibleTrue(String especialidad);
}