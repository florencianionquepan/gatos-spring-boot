package com.example.gatosspringboot.repository.database;

import com.example.gatosspringboot.model.Usuario;
import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepository extends CrudRepository<Usuario,Long> {

}
