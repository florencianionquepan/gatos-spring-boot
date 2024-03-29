package com.example.gatosspringboot.dto;

import com.example.gatosspringboot.model.Padrino;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
//para crear y editar gato
//queda mapear Padrino y solicitudes
@JsonIgnoreProperties(ignoreUnknown = true)
public class GatoDTO {

    private Long id;
    @NotNull
    @NotEmpty
    @Pattern(regexp = "^[A-Za-z]+$", message = "Solamente permite caracteres de la A - Z")
    private String nombre;
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
    private Double montoMensual;

    private FichaDTO ficha;

    @JsonIgnoreProperties(value="gato")
    private List<SolicitudReqDTO> solicitudes;

    @Valid
    private VoluntarioEmailDTO voluntario;

    private PadrinoDTO padrino;

    private LocalDate adoptado;

    public GatoDTO(Long id, LocalDate adoptado, String nombre) {
        this.id = id;
        this.adoptado = adoptado;
        this.nombre = nombre;
    }
}
