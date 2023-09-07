package com.example.gatosspringboot.repository.database;

import com.example.gatosspringboot.model.Socio;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SocioRepository extends CrudRepository<Socio,Long> {
    @Query("SELECT s FROM Socio s WHERE s.persona.email = ?1")
    Optional<Socio> findByEmail(String email);

    @Query("SELECT s FROM Socio s WHERE s.persona.dni = ?1")
    Optional<Socio> findByDni(String dni);
}
