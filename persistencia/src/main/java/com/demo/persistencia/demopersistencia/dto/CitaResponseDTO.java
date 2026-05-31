package com.demo.persistencia.demopersistencia.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Data
public class CitaResponseDTO {
    private Long citaId;
    private Long pacienteId;
    private String pacienteNombre;
    private Long medicoId;
    private String medicoNombre;
    private String especialidad;
    private LocalDate fechaCita;
    private LocalTime horaCita;
    private String ubicacion;
    private String estado;
    private String motivo;
    private String asignadoPor;
    private LocalDateTime fechaAsignacion;
    private String notas;           
    private String diagnostico;     
    private String recomendaciones; 
}