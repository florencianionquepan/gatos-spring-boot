package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.Usuario;

import java.util.List;

public interface IUsuarioService {
    List<Usuario> verTodos();
    Usuario altaUsuario(Usuario usuario);
    Boolean validarUsuario(Long id, String token);
    void enviarValidacion(String email);
    Usuario agregarRolVoluntario(String email);
    String modiPassword(Usuario user, String oldPassword);
    Usuario altaUsuarioSocio(String email);
    Usuario agregarRolSocio(Usuario user);
    Usuario buscarByEmail(String email);
}
