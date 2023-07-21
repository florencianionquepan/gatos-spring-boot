package com.example.gatosspringboot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudReqDTO {

    Long id;
    List<EstadoDTO> estados;

    @Valid
    @JsonIgnoreProperties(value={"solicitudes","gato"})
    PersonaEmailDTO solicitante;
    @Valid
    GatoIdDTO gato;
}
