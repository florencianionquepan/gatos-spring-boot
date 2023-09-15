package com.example.gatosspringboot.repository.database;

import com.example.gatosspringboot.model.Gato;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GatoRepository extends CrudRepository<Gato,Long> {

    @Query("from Gato g WHERE g.voluntario.persona.email = ?1")
    List<Gato> findByVoluntarioEmail(String email);

}
