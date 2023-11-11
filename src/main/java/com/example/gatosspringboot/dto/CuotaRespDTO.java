package com.example.gatosspringboot.dto;

import com.example.gatosspringboot.model.EstadoPago;
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
public class CuotaRespDTO {
    private Long id;
    private LocalDate fechaCreacion;
    private LocalDate fechaPago;
    private double montoMensual;
    @JsonIgnoreProperties(value = "padrino")
    private GatoDTO gato;
    private EstadoPago estadoPago;
}
