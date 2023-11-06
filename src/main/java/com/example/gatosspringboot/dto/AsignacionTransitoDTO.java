package com.example.gatosspringboot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AsignacionTransitoDTO {
    private Long id;
    private LocalDate fechaAsignacion;
    private LocalDate fechaFin;
    @JsonIgnoreProperties(value="solicitudes")
    private PersonaDTO transito;
}
