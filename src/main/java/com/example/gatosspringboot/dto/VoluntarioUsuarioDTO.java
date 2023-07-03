package com.example.gatosspringboot.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//LO USAMOS AL CREAR UN GATO
public class VoluntarioUsuarioDTO {
    @Valid
    private UsuarioEmailDTO usuario;
}
