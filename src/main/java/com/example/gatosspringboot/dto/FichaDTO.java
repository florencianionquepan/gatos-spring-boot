package com.example.gatosspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FichaDTO {

    private Long id;
    private LocalDate ultimaDesparasitacion;
    private LocalDate ultimaTripleFelina;
    private LocalDate ultimaAntirrabica;
    private String comentarios;
}
