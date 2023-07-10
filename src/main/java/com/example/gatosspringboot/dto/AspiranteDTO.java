package com.example.gatosspringboot.dto;

import com.example.gatosspringboot.dto.validator.PostValidationGroup;
import com.example.gatosspringboot.dto.validator.PutValidationGroup;
import com.example.gatosspringboot.dto.validator.ValueOfEnum;
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

    @ValueOfEnum(enumClass=TipoVoluntariado.class, groups = PostValidationGroup.class,
            message = "Debe ser algun valor de los siguientes: 'VOLUNTARIO', 'TRANSITO' o 'PADRINO'")
    //VOLUNTARIO, TRANSITO, PADRINO
    private List<String> tiposVoluntariado;

    @NotNull(groups = PutValidationGroup.class)
    private Socio socio;
}
