package com.example.gatosspringboot.repository.database;

import com.example.gatosspringboot.model.Persona;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PersonaRepository extends CrudRepository<Persona,Long> {

    Optional<Persona> findByDni(String dni);

    Optional<Persona> findByEmail(String email);
}
