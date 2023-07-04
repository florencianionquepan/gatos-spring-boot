package com.example.gatosspringboot.repository.database;

import com.example.gatosspringboot.model.Voluntario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VoluntarioRepository extends CrudRepository<Voluntario, Long> {
    @Query("from Voluntario v where v.dni= ?1")
    Optional<Voluntario> findByDni(String dni);

    @Query("from Voluntario v where v.email= ?1")
    Optional<Voluntario> findByEmail(String email);
}
