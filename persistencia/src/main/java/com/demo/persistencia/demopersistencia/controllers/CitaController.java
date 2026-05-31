package com.demo.persistencia.demopersistencia.controllers;

import com.demo.persistencia.demopersistencia.dto.CitaRequestDTO;
import com.demo.persistencia.demopersistencia.dto.CitaResponseDTO;
import com.demo.persistencia.demopersistencia.entidades.Usuario;
import com.demo.persistencia.demopersistencia.repositorio.UsuarioRepository;
import com.demo.persistencia.demopersistencia.services.CitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;  // ← Agrega esta importación

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CitaController {

    private final CitaService citaService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/asignar")
    public ResponseEntity<CitaResponseDTO> asignarCita(@Valid @RequestBody CitaRequestDTO request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));
        
        CitaResponseDTO response = citaService.asignarCita(request, usuario.getUsuarioId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<CitaResponseDTO>> listarCitasPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(citaService.listarCitasPorPaciente(pacienteId));
    }

    @PatchMapping("/{citaId}/estado")
    public ResponseEntity<CitaResponseDTO> actualizarEstadoCita(
            @PathVariable Long citaId,
            @RequestParam String estado) {
        return ResponseEntity.ok(citaService.actualizarEstadoCita(citaId, estado));
    }

    // NUEVO: Atender cita con notas, diagnóstico y recomendaciones
    @PatchMapping("/{citaId}/atender")
    public ResponseEntity<CitaResponseDTO> atenderCita(
            @PathVariable Long citaId,
            @RequestBody Map<String, String> datos) {  // ← Ahora Map está importado
        
        String notas = datos.get("notas");
        String diagnostico = datos.get("diagnostico");
        String recomendaciones = datos.get("recomendaciones");
        
        CitaResponseDTO response = citaService.atenderCita(citaId, notas, diagnostico, recomendaciones);
        return ResponseEntity.ok(response);
    }
}