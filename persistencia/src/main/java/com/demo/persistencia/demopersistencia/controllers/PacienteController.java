package com.demo.persistencia.demopersistencia.controllers;

import com.demo.persistencia.demopersistencia.dto.PacienteRequestDTO;
import com.demo.persistencia.demopersistencia.dto.PacienteResponseDTO;
import com.demo.persistencia.demopersistencia.services.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PacienteController {

    private final PacienteService pacienteService;

    @PostMapping("/registrar")
    public ResponseEntity<PacienteResponseDTO> registrarPaciente(@Valid @RequestBody PacienteRequestDTO request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PacienteResponseDTO response = pacienteService.registrarPaciente(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PacienteResponseDTO>> listarPacientes() {
        return ResponseEntity.ok(pacienteService.listarPacientes());
    }

    @GetMapping("/{id}")
public ResponseEntity<PacienteResponseDTO> obtenerPaciente(@PathVariable Long id) {
    return ResponseEntity.ok(pacienteService.obtenerPaciente(id));
}
}