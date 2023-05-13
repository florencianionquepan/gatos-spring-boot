package com.example.gatosspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoluntarioUsuarioDTO {
    @Valid
    private UsuarioEmailDTO usuario;
}
