package com.example.gatosspringboot.model;

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
    private Gato gato;

    @ManyToOne
    @JoinColumn(name = "transito_id")
    private Transito transito;

    private LocalDate fechaAsociacion;
}
