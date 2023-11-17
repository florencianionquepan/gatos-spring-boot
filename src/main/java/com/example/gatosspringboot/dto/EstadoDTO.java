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
public class EstadoDTO {

    private LocalDate fecha;
    private String estado;
    private String motivo;
}
