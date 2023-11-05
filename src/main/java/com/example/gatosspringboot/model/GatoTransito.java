package com.example.gatosspringboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name="gatos_transitos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GatoTransito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "gato_id")
    @JsonIgnoreProperties(value="asignacionesTransitos")
    private Gato gato;

    @ManyToOne
    @JoinColumn(name = "transito_id")
    @JsonIgnoreProperties(value="asignacionesGatos")
    private Transito transito;

    private LocalDate fechaAsociacion;
    private LocalDate fechaFin;

    @PrePersist
    public void prePersist() {
        fechaAsociacion = LocalDate.now();
    }
}
