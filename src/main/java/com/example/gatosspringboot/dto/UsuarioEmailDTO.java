package com.example.gatosspringboot.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioEmailDTO {
    @NotNull
    @Email
    private String mail;
}
