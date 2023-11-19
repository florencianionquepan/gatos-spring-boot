package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.Persona;
import com.example.gatosspringboot.model.Usuario;

import java.util.HashMap;
import java.util.List;

public interface IUsuarioService {
    HashMap<Usuario, Persona> verTodos();
    Usuario bloquearUsuario(Long id, String motivo);
    Usuario desbloquearUsuario(Long id, String motivo);
    Usuario altaUsuario(Usuario usuario);
    Boolean validarUsuario(Long id, String token);
    void enviarValidacion(String email);
    Usuario agregarRolVoluntario(String email);
    String modiPassword(Usuario user, String oldPassword);
    //Usuario altaUsuarioSocio(String email);
    Usuario agregarRolSocio(Long id);
    Usuario buscarByEmail(String email);
}
