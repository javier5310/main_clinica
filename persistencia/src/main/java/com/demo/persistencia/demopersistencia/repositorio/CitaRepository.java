package com.demo.persistencia.demopersistencia.repositorio;

import com.demo.persistencia.demopersistencia.entidades.Cita;
import com.demo.persistencia.demopersistencia.entidades.Paciente;
import com.demo.persistencia.demopersistencia.entidades.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByPaciente(Paciente paciente);
    List<Cita> findByMedicoAndFechaCita(Medico medico, LocalDate fechaCita);
    List<Cita> findByMedicoAndFechaCitaAndEstado(Medico medico, LocalDate fechaCita, String estado);
    List<Cita> findByMedicoOrderByFechaCitaDesc(Medico medico);
    boolean existsByMedicoAndFechaCitaAndHoraCita(Medico medico, LocalDate fechaCita, java.time.LocalTime horaCita);
}