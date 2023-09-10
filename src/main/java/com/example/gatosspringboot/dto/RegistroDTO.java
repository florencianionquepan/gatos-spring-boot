package com.example.gatosspringboot.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroDTO {
    private Long id;
    @NotNull
    @NotEmpty
    @Pattern(regexp = "^[0-9]+$",message = "Solo se permiten numeros")
    @Size(min=8,max=8,message = "Debe contener 8 caracteres")
    private String dni;
    @NotNull
    @NotEmpty
    private String nombre;
    @NotNull
    @NotEmpty
    private String apellido;
    @NotNull
    @NotEmpty
    private String tel;
    @NotNull
    private LocalDate fechaNac;
    @NotNull
    @NotEmpty
    private String dire;
    @NotNull
    @NotEmpty
    private String localidad;

    @Valid
    private UsuarioReqDTO usuario;
/*
    @NotNull
    private String token;*/
}
