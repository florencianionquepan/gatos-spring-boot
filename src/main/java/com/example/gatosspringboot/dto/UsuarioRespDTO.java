package com.example.gatosspringboot.dto;

import com.example.gatosspringboot.model.Rol;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRespDTO {
    Long id;
    PersonaDTO persona;
    Boolean verificado;
    Boolean habilitado;
    @NotNull
    String motivo;
    List<RolDTO> roles;
}
