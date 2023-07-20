package com.example.gatosspringboot.dto;

import com.example.gatosspringboot.model.Estado;
import com.example.gatosspringboot.model.Gato;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudDTO {

    Long id;
    List<Estado> estados;

    @Valid
    @JsonIgnoreProperties(value={"solicitudes","gato"})
    PersonaDTO solicitante;
    @Valid
    GatoIdDTO gato;
}
