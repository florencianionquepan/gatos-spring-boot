package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.Ficha;
import org.springframework.web.multipart.MultipartFile;

public interface IFichaService {
    Ficha crear(Ficha ficha, MultipartFile file);
    Ficha editar(Ficha ficha,MultipartFile file, Long idFicha);
}
