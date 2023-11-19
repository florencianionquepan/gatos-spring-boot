package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.RolDTO;
import com.example.gatosspringboot.model.Rol;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RolMapper implements IRolMapper{
    @Override
    public RolDTO mapToRolDTO(Rol rol) {
        RolDTO dto=new RolDTO();
        dto.setId(rol.getId());
        dto.setNombre(rol.getNombre());
        return dto;
    }

    @Override
    public List<RolDTO> mapToListRolDto(List<Rol> roles) {
        return roles.stream()
                .map(this::mapToRolDTO)
                .collect(Collectors.toList());
    }
}
