package com.example.gatosspringboot.repository.database;

import com.example.gatosspringboot.model.Padrino;
import com.example.gatosspringboot.model.Socio;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PadrinoRepository extends CrudRepository<Padrino,Long> {
    @Query("from Padrino p where p.persona.email= ?1")
    Optional<Padrino> buscarByEmail(String email);

    @Query("from Padrino p where p.persona.dni= ?1")
    Optional<Padrino> findByDni(String dni);
}
