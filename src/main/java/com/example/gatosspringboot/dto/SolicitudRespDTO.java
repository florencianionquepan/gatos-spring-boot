package com.example.gatosspringboot.dto;

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
public class SolicitudRespDTO {

    Long id;
    List<EstadoDTO> estados;

    @JsonIgnoreProperties(value="solicitudes")
    PersonaDTO solicitante;

    @JsonIgnoreProperties(value="solicitudes")
    GatoRespDTO gato;
}
