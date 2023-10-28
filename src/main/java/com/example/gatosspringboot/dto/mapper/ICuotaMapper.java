package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.CuotaDTO;
import com.example.gatosspringboot.model.Cuota;

public interface ICuotaMapper {
    Cuota mapToEntity(CuotaDTO dto);
}
