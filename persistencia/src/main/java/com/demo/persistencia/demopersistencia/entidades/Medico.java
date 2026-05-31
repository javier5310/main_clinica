package com.demo.persistencia.demopersistencia.entidades;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "medicos", schema = "clinica")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medico_id")
    private Long medicoId;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "especialidad", nullable = false, length = 100)
    private String especialidad;

    @Column(name = "disponible")
    private Boolean disponible = true;

    @Column(name = "telefono_contacto", length = 20)
    private String telefonoContacto;

    @Column(name = "horario_atencion", length = 200)
    private String horarioAtencion;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
    }
}