package com.example.gatosspringboot.dto;

import com.example.gatosspringboot.dto.validator.PostValidationGroup;
import com.example.gatosspringboot.dto.validator.PutValidationGroup;
import com.example.gatosspringboot.dto.validator.ValueOfEnum;
import com.example.gatosspringboot.model.Estado;
import com.example.gatosspringboot.model.Socio;
import com.example.gatosspringboot.model.TipoVoluntariado;
import jakarta.validation.Valid;
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
public class SolicitudVoluntariadoDTO {
    private Long id;

    @Valid
    private PersonaDTO aspirante;

    @NotNull
    @ValueOfEnum(enumClass=TipoVoluntariado.class, groups = PostValidationGroup.class,
            message = "Debe ser de tipo 'VOLUNTARIO' o 'TRANSITO'.")
    private String voluntariado;

    private List<String> estados;

    @NotNull(groups = PutValidationGroup.class)
    private Socio socio;
}
