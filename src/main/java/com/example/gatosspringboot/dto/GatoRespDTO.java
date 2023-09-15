package com.example.gatosspringboot.dto;

import com.example.gatosspringboot.model.Padrino;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class GatoRespDTO {
    private Long id;
    private String nombre;
    private List<String> fotos;
    private String edad;
    private String sexo;
    private String descripcion;
    private String color;
    private String tipoPelo;
    private Double montoMensual;

    private FichaDTO ficha;

    @JsonIgnoreProperties(value="gato")
    private List<SolicitudReqDTO> solicitudes;
    @JsonIgnoreProperties(value = {"gatos","solicitudes"})
    private VoluntarioDTO voluntario;
    @JsonIgnoreProperties(value={"gatos","solicitudes","cuotas"})
    private PadrinoDTO padrino;
    @JsonIgnoreProperties(value={"gatos","solicitudes"})
    private TransitoRespDTO transito;

    private LocalDate adoptado;
}
