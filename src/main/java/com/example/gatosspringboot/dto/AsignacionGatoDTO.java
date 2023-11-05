package com.example.gatosspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//para devolver a un transito
public class AsignacionGatoDTO {
    private Long id;
    private LocalDate fechaAsignacion;
    private LocalDate fechaFin;
    private GatoDTO gato;

}
