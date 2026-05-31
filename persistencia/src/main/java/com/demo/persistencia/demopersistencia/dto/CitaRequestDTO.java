package com.demo.persistencia.demopersistencia.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CitaRequestDTO {
    @NotNull(message = "El ID del paciente es obligatorio")
    private Long pacienteId;
    
    @NotNull(message = "El ID del médico es obligatorio")
    private Long medicoId;
    
    @NotNull(message = "La fecha de la cita es obligatoria")
    private LocalDate fechaCita;
    
    @NotNull(message = "La hora de la cita es obligatoria")
    private LocalTime horaCita;
    
    private String motivo;
}