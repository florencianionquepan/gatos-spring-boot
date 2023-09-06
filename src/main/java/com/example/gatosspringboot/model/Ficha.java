package com.example.gatosspringboot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name="fichas_vets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ficha implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate ultimaDesparasitacion;
    private LocalDate ultimaTripleFelina;
    private LocalDate ultimaAntirrabica;
    private String comentarios;
    private String pdf;

}
