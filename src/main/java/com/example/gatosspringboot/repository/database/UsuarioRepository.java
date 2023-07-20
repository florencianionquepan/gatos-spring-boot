package com.example.gatosspringboot.repository.database;

import com.example.gatosspringboot.model.Usuario;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario,Long> {

    @Query("from Usuario u where u.email= ?1")
    Optional<Usuario> findByEmail(String email);
}
