package com.example.gatosspringboot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class VoluntarioEmailDTO {
    @NotNull
    @Email
    private String email;
}
