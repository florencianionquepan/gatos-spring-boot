package com.example.gatosspringboot.dto;

import com.example.gatosspringboot.dto.validator.ValueOfEnum;
import com.example.gatosspringboot.model.EstadoNombre;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstadoDTO {

    private LocalDate fecha;

/*    @NotNull
    @ValueOfEnum(enumClass= EstadoNombre.class,
            message = "Debe ser de tipo 'PENDIENTE','APROBADA' o 'RECHAZADA' o 'CERRADA'.")*/
    private String estado;

    private String motivo;
}
