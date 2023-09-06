package com.example.gatosspringboot.repository.database;

import com.example.gatosspringboot.model.Persona;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PersonaRepository extends CrudRepository<Persona,Long> {
    @Query("from Persona p where p.dni= ?1")
    Optional<Persona> findByDni(String dni);

    @Query("from Persona p where p.email= ?1")
    Optional<Persona> findByEmail(String email);
}
