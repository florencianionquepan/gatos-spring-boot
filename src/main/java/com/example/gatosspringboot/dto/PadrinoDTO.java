package com.example.gatosspringboot.dto;

import com.example.gatosspringboot.model.Cuota;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PadrinoDTO extends PersonaDTO {

    @JsonIgnoreProperties(value = "padrino")
    private List<GatoDTO> gatos;

    @JsonIgnoreProperties(value = "padrino")
    private List<Cuota> cuotas;
}
