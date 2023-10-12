package com.example.gatosspringboot.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TransitoRespDTO extends PersonaDTO{
    List<GatoIdDTO> gatos;
}
