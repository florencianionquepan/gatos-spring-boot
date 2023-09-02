package com.example.gatosspringboot.repository.database;

import com.example.gatosspringboot.model.Padrino;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PadrinoRepository extends CrudRepository<Padrino,Long> {
    Optional<Padrino> findByEmail(String email);
}
