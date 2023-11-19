package com.example.gatosspringboot.dto;

import com.example.gatosspringboot.model.Rol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRespDTO {
    PersonaDTO persona;
    boolean verificado;
    boolean bloqueado;
    List<RolDTO> roles;
}
