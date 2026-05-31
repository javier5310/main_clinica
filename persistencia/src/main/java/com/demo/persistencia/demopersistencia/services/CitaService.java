package com.demo.persistencia.demopersistencia.services;

import com.demo.persistencia.demopersistencia.dto.CitaRequestDTO;
import com.demo.persistencia.demopersistencia.dto.CitaResponseDTO;
import com.demo.persistencia.demopersistencia.entidades.Cita;
import com.demo.persistencia.demopersistencia.entidades.Medico;
import com.demo.persistencia.demopersistencia.entidades.Paciente;
import com.demo.persistencia.demopersistencia.entidades.Usuario;
import com.demo.persistencia.demopersistencia.repositorio.CitaRepository;
import com.demo.persistencia.demopersistencia.repositorio.MedicoRepository;
import com.demo.persistencia.demopersistencia.repositorio.PacienteRepository;
import com.demo.persistencia.demopersistencia.repositorio.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CitaService {

    private final CitaRepository citaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public CitaResponseDTO asignarCita(CitaRequestDTO request, Long usuarioAsignoId) {
        // 1. Buscar el médico (request.getMedicoId() ahora es Long)
        Medico medico = medicoRepository.findById(request.getMedicoId())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado con ID: " + request.getMedicoId()));

        // 2. Verificar si el médico está disponible
        if (!medico.getDisponible()) {
            throw new RuntimeException("El médico no está disponible en este momento");
        }

        // 3. Validar que no haya una cita duplicada en el mismo horario
        boolean existeCita = citaRepository.existsByMedicoAndFechaCitaAndHoraCita(
                medico, request.getFechaCita(), request.getHoraCita());
        
        if (existeCita) {
            throw new RuntimeException("El médico ya tiene una cita agendada en ese horario");
        }

        // 4. Buscar el paciente
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + request.getPacienteId()));

        // 5. Buscar el usuario que asigna la cita (recepcionista)
        Usuario usuarioAsigno = usuarioRepository.findById(usuarioAsignoId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioAsignoId));

        // 6. Crear la cita
        Cita cita = new Cita();
        cita.setPaciente(paciente);
        cita.setMedico(medico);
        cita.setFechaCita(request.getFechaCita());
        cita.setHoraCita(request.getHoraCita());
        cita.setMotivo(request.getMotivo());
        cita.setEstado("PENDIENTE");
        cita.setUsuarioAsigno(usuarioAsigno);
        cita.setUbicacion(medico.getHorarioAtencion());

        // 7. Guardar la cita
        Cita saved = citaRepository.save(cita);
        
        // 8. Convertir a DTO para la respuesta
        return convertToResponseDTO(saved);
    }

    public List<CitaResponseDTO> listarCitasPorPaciente(Long pacienteId) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + pacienteId));
        
        return citaRepository.findByPaciente(paciente).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<CitaResponseDTO> listarCitasPorMedico(Long medicoId) {
    Medico medico = medicoRepository.findById(medicoId)
            .orElseThrow(() -> new RuntimeException("Médico no encontrado"));
    
    return citaRepository.findByMedicoOrderByFechaCitaDesc(medico).stream()
            .map(this::convertToResponseDTO)
            .collect(Collectors.toList());
    }


    @Transactional
    public CitaResponseDTO actualizarEstadoCita(Long citaId, String nuevoEstado) {
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + citaId));
        
        cita.setEstado(nuevoEstado);
        return convertToResponseDTO(citaRepository.save(cita));
    }

    @Transactional
public CitaResponseDTO atenderCita(Long citaId, String notas, String diagnostico, String recomendaciones) {
    Cita cita = citaRepository.findById(citaId)
            .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
    
    cita.setNotas(notas);
    cita.setDiagnostico(diagnostico);
    cita.setRecomendaciones(recomendaciones);
    cita.setEstado("ATENDIDA");
    
    return convertToResponseDTO(citaRepository.save(cita));
    }


    private CitaResponseDTO convertToResponseDTO(Cita cita) {
        CitaResponseDTO dto = new CitaResponseDTO();
        
        // IDs - Directamente sin conversión
        dto.setCitaId(cita.getCitaId());
        dto.setPacienteId(cita.getPaciente().getPacienteId());
        dto.setMedicoId(cita.getMedico().getMedicoId());
        
        // Datos del paciente
        dto.setPacienteNombre(cita.getPaciente().getNombreCompleto());
        
        // Datos del médico
        dto.setMedicoNombre(cita.getMedico().getUsuario().getNombreCompleto());
        dto.setEspecialidad(cita.getMedico().getEspecialidad());
        
        // Datos de la cita
        dto.setFechaCita(cita.getFechaCita());
        dto.setHoraCita(cita.getHoraCita());
        dto.setUbicacion(cita.getUbicacion());
        dto.setEstado(cita.getEstado());
        dto.setMotivo(cita.getMotivo());
        
        // Usuario que asignó
        dto.setAsignadoPor(cita.getUsuarioAsigno().getNombreCompleto());
        dto.setFechaAsignacion(cita.getFechaAsignacion());
        
        return dto;
    }
}