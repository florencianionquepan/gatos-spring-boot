package com.example.gatosspringboot.dto;

import com.example.gatosspringboot.dto.validator.FieldTipoVoluntariado;
import com.example.gatosspringboot.dto.validator.PutValidationGroup;
import com.example.gatosspringboot.model.Socio;
import com.example.gatosspringboot.model.TipoVoluntariado;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AspiranteDTO extends PersonaDTO{
    @FieldTipoVoluntariado
    @NotNull
    private List<TipoVoluntariado> tiposVoluntariado;

    @NotNull(groups = PutValidationGroup.class)
    private Socio socio;
}
