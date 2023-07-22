package com.example.gatosspringboot.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//Para cargar al gato creado
public class TransitoIdDTO {
    @NotNull
    private Long id;
}
