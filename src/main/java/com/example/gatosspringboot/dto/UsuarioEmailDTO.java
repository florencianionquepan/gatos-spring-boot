package com.example.gatosspringboot.dto;

import lombok.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

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
