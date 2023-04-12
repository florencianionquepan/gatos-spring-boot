package com.example.gatosspringboot.dto;

import com.example.gatosspringboot.model.Gato;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoluntarioDTO extends PersonaDTO {

    private UsuarioRespDTO usuario;
}
