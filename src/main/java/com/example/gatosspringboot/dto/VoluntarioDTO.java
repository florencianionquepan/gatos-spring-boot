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
//Se utiliza para dar de alta y listar (se agregan gatos ahi)
//necesita datos de persona + email
public class VoluntarioDTO extends PersonaDTO {

    @JsonIgnoreProperties(value = "voluntario")
    private List<GatoDTO> gatos;
}
