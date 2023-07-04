package com.example.gatosspringboot.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//LO USAMOS AL CREAR UN GATO
public class VoluntarioEmailDTO {
    @NotNull
    @Email
    private String email;
}
