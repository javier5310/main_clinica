package com.demo.persistencia.demopersistencia.entidades;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "citas", schema = "clinica")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cita_id")
    private Long citaId;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @Column(name = "fecha_cita", nullable = false)
    private LocalDate fechaCita;

    @Column(name = "hora_cita", nullable = false)
    private LocalTime horaCita;

    @Column(name = "ubicacion", length = 200)
    private String ubicacion;

    @Column(name = "motivo", length = 255)
    private String motivo;

    @Column(name = "estado", length = 20)
    private String estado = "PENDIENTE";

    @ManyToOne
    @JoinColumn(name = "usuario_asigno_id")
    private Usuario usuarioAsigno;

    @Column(name = "fecha_asignacion")
    private LocalDateTime fechaAsignacion;

    @Column(name = "notas", columnDefinition = "TEXT")
    private String notas;  // ← NUEVO CAMPO para comentarios del médico

    @Column(name = "diagnostico", columnDefinition = "TEXT")
    private String diagnostico;  // ← NUEVO CAMPO

    @Column(name = "recomendaciones", columnDefinition = "TEXT")
    private String recomendaciones;  // ← NUEVO CAMPO

    @PrePersist
    protected void onCreate() {
        fechaAsignacion = LocalDateTime.now();
    }
}