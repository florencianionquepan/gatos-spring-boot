package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IGatoService {

    List<Gato> verTodos();
    Gato verById(Long id);
    List<Gato> verByVoluntario(String email);
    Gato altaGato(Gato gato, MultipartFile[] fotos);
    Gato modiGato(Gato gato,MultipartFile[] files, Long id);
    boolean existeGato(Long id);
    Gato adoptarGato(Long id);
    Gato buscarDisponibleById(Long id);
    Gato agregarFicha(Ficha ficha,MultipartFile file, Long id);
    Gato agregarTransito(Transito transito, Long id);
    Ficha verFichaByGato(Long id);
    List<GatoTransito> asignaciones(Long idGato);
}
