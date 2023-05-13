package com.example.gatosspringboot.dto;

import com.example.gatosspringboot.model.Ficha;
import com.example.gatosspringboot.model.Padrino;
import com.example.gatosspringboot.model.Solicitud;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GatoDTO {

    private Long id;
    @NotNull
    @NotEmpty
    @Pattern(regexp = "^[A-Za-z]+$", message = "Solamente permite caracteres de la A - Z")
    private String nombre;
    @NotNull
    @NotEmpty
    private List<String> fotos;
    @NotNull
    @NotEmpty
    private String edad;
    @NotNull
    @NotEmpty
    private String sexo;
    private String descripcion;
    @NotNull
    @NotEmpty
    private String color;
    @NotNull
    @NotEmpty
    private String tipoPelo;

    private Ficha fichaVet;

    private List<Solicitud> solicitudes;

    @Valid
    private VoluntarioUsuarioDTO voluntario;

    private Padrino padrino;

    private LocalDate adoptado;
}
