package com.demo.persistencia.demopersistencia.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PacienteResponseDTO {
    private Long pacienteId;  
    private String nombreCompleto;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;
    private String seguroMedico;
    private String numeroIdentificacion;
    private String email;
    private String registradoPor;
    private LocalDateTime fechaRegistro;
}