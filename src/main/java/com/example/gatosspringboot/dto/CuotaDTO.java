package com.example.gatosspringboot.dto;

import com.example.gatosspringboot.model.Gato;
import com.example.gatosspringboot.model.Padrino;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
