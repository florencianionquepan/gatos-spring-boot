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
public class CuotaDTO {
    private Long id;
    private LocalDate fechaPago;
    private double montoMensual;
    @JsonIgnoreProperties(value = "cuotas")
    private PadrinoDTO padrino;
    @JsonIgnoreProperties(value = "padrino")
    private GatoDTO gato;
}
