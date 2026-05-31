package com.demo.persistencia.demopersistencia.services;

import com.demo.persistencia.demopersistencia.dto.MedicoResponseDTO;
import com.demo.persistencia.demopersistencia.entidades.Medico;
import com.demo.persistencia.demopersistencia.repositorio.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicoService {

    private final MedicoRepository medicoRepository;

    public List<MedicoResponseDTO> listarMedicosDisponibles() {
        return medicoRepository.findByDisponibleTrue().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<MedicoResponseDTO> listarMedicosPorEspecialidad(String especialidad) {
        return medicoRepository.findByEspecialidadAndDisponibleTrue(especialidad).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private MedicoResponseDTO convertToResponseDTO(Medico medico) {
        MedicoResponseDTO dto = new MedicoResponseDTO();
        dto.setMedicoId(medico.getMedicoId());
        dto.setNombreCompleto(medico.getUsuario().getNombreCompleto());
        dto.setEspecialidad(medico.getEspecialidad());
        dto.setDisponible(medico.getDisponible());
        dto.setTelefonoContacto(medico.getTelefonoContacto());
        dto.setHorarioAtencion(medico.getHorarioAtencion());
        return dto;
    }
}