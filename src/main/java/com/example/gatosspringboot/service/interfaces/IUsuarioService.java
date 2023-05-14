package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.Usuario;

import java.util.List;

public interface IUsuarioService {
    List<Usuario> verTodos();
    String altaUsuarioCompleto(Usuario us);
    String altaUsuarioEmail(String email);
}
