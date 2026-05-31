package com.demo.persistencia.demopersistencia.controllers;

import com.demo.persistencia.demopersistencia.dto.MedicoResponseDTO;
import com.demo.persistencia.demopersistencia.services.MedicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MedicoController {

    private final MedicoService medicoService;

    @GetMapping("/disponibles")
    public ResponseEntity<List<MedicoResponseDTO>> listarMedicosDisponibles() {
        return ResponseEntity.ok(medicoService.listarMedicosDisponibles());
    }

    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<List<MedicoResponseDTO>> listarMedicosPorEspecialidad(@PathVariable String especialidad) {
        return ResponseEntity.ok(medicoService.listarMedicosPorEspecialidad(especialidad));
    }
}