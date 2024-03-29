package com.example.gatosspringboot.repository.database;

import com.example.gatosspringboot.model.Voluntario;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VoluntarioRepository extends CrudRepository<Voluntario, Long> {
    @Query("from Voluntario v where v.persona.dni= ?1")
    Optional<Voluntario> findByDni(String dni);

    @Query("from Voluntario v where v.persona.email= ?1")
    Optional<Voluntario> findByEmail(String email);
}
