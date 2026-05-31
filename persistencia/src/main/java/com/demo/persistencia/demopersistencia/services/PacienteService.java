package com.demo.persistencia.demopersistencia.services;

import com.demo.persistencia.demopersistencia.dto.PacienteRequestDTO;
import com.demo.persistencia.demopersistencia.dto.PacienteResponseDTO;
import com.demo.persistencia.demopersistencia.entidades.Paciente;
import com.demo.persistencia.demopersistencia.entidades.Usuario;
import com.demo.persistencia.demopersistencia.repositorio.PacienteRepository;
import com.demo.persistencia.demopersistencia.repositorio.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public PacienteResponseDTO registrarPaciente(PacienteRequestDTO request, String username) {
        // Buscar usuario por username
        Usuario usuarioRegistro = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));
        
        // Generar número de identificación único
        String numeroIdentificacion = generarNumeroIdentificacion();
        
        // Crear y llenar el paciente
        Paciente paciente = new Paciente();
        paciente.setNombreCompleto(request.getNombreCompleto());
        paciente.setFechaNacimiento(request.getFechaNacimiento());
        paciente.setDireccion(request.getDireccion());
        paciente.setTelefono(request.getTelefono());
        paciente.setSeguroMedico(request.getSeguroMedico());
        paciente.setNumeroIdentificacion(numeroIdentificacion);
        paciente.setEmail(request.getEmail());
        paciente.setUsuarioRegistro(usuarioRegistro);  // Asigna el usuario que registró

        // Guardar en la base de datos
        Paciente saved = pacienteRepository.save(paciente);
        
        // Convertir a DTO para la respuesta
        return convertToResponseDTO(saved);
    }

    public List<PacienteResponseDTO> listarPacientes() {
        return pacienteRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public PacienteResponseDTO obtenerPaciente(Long id) {  // ← Long, no Integer
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + id));
        return convertToResponseDTO(paciente);
    }

    private String generarNumeroIdentificacion() {
        String año = String.valueOf(java.time.Year.now().getValue());
        long count = pacienteRepository.count() + 1;
        return String.format("PAC-%s-%05d", año, count);
    }

    private PacienteResponseDTO convertToResponseDTO(Paciente paciente) {
        PacienteResponseDTO dto = new PacienteResponseDTO();
        
        // Ahora paciente.getPacienteId() es Long, se asigna directamente sin conversión
        dto.setPacienteId(paciente.getPacienteId());  // ← Long → Long, directo
        
        dto.setNombreCompleto(paciente.getNombreCompleto());
        dto.setFechaNacimiento(paciente.getFechaNacimiento());
        dto.setDireccion(paciente.getDireccion());
        dto.setTelefono(paciente.getTelefono());
        dto.setSeguroMedico(paciente.getSeguroMedico());
        dto.setNumeroIdentificacion(paciente.getNumeroIdentificacion());
        dto.setEmail(paciente.getEmail());
        
        // Obtener nombre del usuario que registró
        dto.setRegistradoPor(paciente.getUsuarioRegistro() != null ? 
                paciente.getUsuarioRegistro().getNombreCompleto() : "Sistema");
        
        dto.setFechaRegistro(paciente.getFechaRegistro());
        
        return dto;
    }
}