package com.demo.persistencia.demopersistencia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String refreshToken;
    private String username;
    private String rol;
    private String nombreCompleto;
    private Long usuarioId;
}