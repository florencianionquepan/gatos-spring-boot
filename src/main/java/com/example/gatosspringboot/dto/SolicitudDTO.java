package com.example.gatosspringboot.dto;

import com.example.gatosspringboot.model.Gato;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudDTO {
    @Valid
    PersonaDTO solicitante;
    @Valid
    GatoIdDTO gato;
}
