package com.demo.persistencia.demopersistencia.repositorio;

import com.demo.persistencia.demopersistencia.entidades.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByNumeroIdentificacion(String numeroIdentificacion);
    boolean existsByNumeroIdentificacion(String numeroIdentificacion);
}