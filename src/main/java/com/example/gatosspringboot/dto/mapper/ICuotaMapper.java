package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.CuotaDTO;
import com.example.gatosspringboot.dto.CuotaRespDTO;
import com.example.gatosspringboot.model.Cuota;

import java.util.List;

public interface ICuotaMapper {
    Cuota mapToEntity(CuotaDTO dto);
    CuotaRespDTO mapToDto(Cuota entity);
    List<CuotaRespDTO> mapToListDto(List<Cuota> entities);
}
