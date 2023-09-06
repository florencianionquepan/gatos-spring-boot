package com.example.gatosspringboot.dto;

import com.example.gatosspringboot.model.SolicitudAdopcion;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonaDTO {
    private Long id;
    @NotNull
    @NotEmpty
    @Pattern(regexp = "^[0-9]+$",message = "Solo se permiten numeros")
    @Size(min=8,max=8)
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
    @NotEmpty
    @Email
    private String email;
    @NotNull
    private LocalDate fechaNac;
    @NotNull
    @NotEmpty
    private String dire;
    @NotNull
    @NotEmpty
    private String localidad;
    @JsonIgnoreProperties(value={"solicitante","gato"})
    private List<SolicitudAdopcion> solicitudes;
}
