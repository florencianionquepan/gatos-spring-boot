package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.Voluntario;

import java.util.List;

public interface IVoluntarioService {
    List<Voluntario> verTodos();
    Voluntario altaVolunt(Voluntario vol);
    Voluntario modiVolunt(Voluntario vol, Long id);
    boolean existeVol(Long id);
    Voluntario buscarVolByEmailOrException(String email);
}
