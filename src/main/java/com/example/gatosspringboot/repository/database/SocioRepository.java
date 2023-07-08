package com.example.gatosspringboot.repository.database;

import com.example.gatosspringboot.model.Socio;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SocioRepository extends CrudRepository<Socio,Long> {
    @Query("from Socio s where s.email= ?1")
    Optional<Socio> findByEmail(String email);
}
