package com.example.gatosspringboot.dto;

import com.example.gatosspringboot.model.Rol;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    String email;
    String nombre;
    String localidad;
    @JsonIgnoreProperties(value = "usuarios")
    List<Rol> roles;
    boolean esTransito;
    boolean esPadrino;
}
