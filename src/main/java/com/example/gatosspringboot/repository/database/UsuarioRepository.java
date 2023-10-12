package com.example.gatosspringboot.repository.database;

import com.example.gatosspringboot.model.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario,Long> {

    Optional<Usuario> findByEmail(String email);
}
