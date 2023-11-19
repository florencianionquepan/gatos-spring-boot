package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.RolDTO;
import com.example.gatosspringboot.model.Rol;

import java.util.List;

public interface IRolMapper {
    RolDTO mapToRolDTO(Rol rol);
    List<RolDTO> mapToListRolDto(List<Rol> roles);
}
