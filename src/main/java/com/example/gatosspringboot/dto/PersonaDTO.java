package com.example.gatosspringboot.dto;

import com.example.gatosspringboot.model.Solicitud;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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
    private String email;
    @NotNull
    @NotEmpty
    private LocalDate fechaNac;
    @NotNull
    @NotEmpty
    private String dire;
    @NotNull
    @NotEmpty
    private String localidad;
    private List<Solicitud> solicitudes;
}
