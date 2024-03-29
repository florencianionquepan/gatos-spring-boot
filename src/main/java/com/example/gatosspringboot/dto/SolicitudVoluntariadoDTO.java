package com.example.gatosspringboot.dto;

import com.example.gatosspringboot.dto.validator.PostValidationGroup;
import com.example.gatosspringboot.dto.validator.PutValidationGroup;
import com.example.gatosspringboot.dto.validator.ValueOfEnum;
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
public class SolicitudVoluntariadoDTO {
    private Long id;

    @NotNull(groups = PostValidationGroup.class)
    private PersonaDTO aspirante;

    @NotNull
    @ValueOfEnum(enumClass=TipoVoluntariado.class, groups = PostValidationGroup.class,
            message = "Debe ser de tipo 'VOLUNTARIO' o 'TRANSITO'.")
    private String voluntariado;

    private List<EstadoDTO> estados;

    @NotNull(groups = PutValidationGroup.class)
    private SocioDTO socio;

    @NotNull(groups = PutValidationGroup.class)
    private String motivo;

    @Override
    public String toString() {
        return "SolicitudVoluntariadoDTO{" +
                "id=" + id +
                ", aspirante=" + aspirante +
                ", voluntariado='" + voluntariado + '\'' +
                ", estados=" + estados +
                ", socio=" + socio +
                ", motivo='" + motivo + '\'' +
                '}';
    }
}
