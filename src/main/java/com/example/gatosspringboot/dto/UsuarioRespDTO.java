package com.example.gatosspringboot.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioRespDTO {
    @NotNull
    @Email
    private String mail;
}
