package com.demo.persistencia.demopersistencia.dto;

import lombok.Data;

@Data
public class MedicoResponseDTO {
    private Long medicoId;
    private String nombreCompleto;
    private String especialidad;
    private Boolean disponible;
    private String telefonoContacto;
    private String horarioAtencion;
}