package com.example.gatosspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.Valid;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//Se utiliza para dar de alta
//necesita datos de persona + email
public class VoluntarioDTO extends PersonaDTO {
    //@Valid
    //private UsuarioEmailDTO usuario;
}
