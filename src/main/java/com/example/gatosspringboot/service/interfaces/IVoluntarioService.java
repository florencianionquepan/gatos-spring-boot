package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.Usuario;
import com.example.gatosspringboot.model.Voluntario;

import java.util.List;
import java.util.Optional;

public interface IVoluntarioService {
    List<Voluntario> verTodos();
    Voluntario altaVolunt(Voluntario vol);
    Voluntario modiVolunt(Voluntario vol, Long id);
    boolean existeVol(Long id);
    Voluntario buscarVolByEmail(String email);

}
