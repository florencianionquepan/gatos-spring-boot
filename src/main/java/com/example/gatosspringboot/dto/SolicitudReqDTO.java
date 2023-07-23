package com.example.gatosspringboot.dto;

import com.example.gatosspringboot.dto.validator.PostValidationGroup;
import com.example.gatosspringboot.dto.validator.PutValidationGroup;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(groups = PostValidationGroup.class)
    @JsonIgnoreProperties(value={"solicitudes","gato"})
    PersonaEmailDTO solicitante;

    @Valid
    @NotNull(groups = PostValidationGroup.class)
    GatoIdDTO gato;

    @NotNull(groups = PutValidationGroup.class)
    private String motivo;
}
