package com.example.gatosspringboot.repository.database;

import com.example.gatosspringboot.model.Transito;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TransitoRepository extends CrudRepository<Transito,Long> {

    @Query("from Transito t where t.persona.localidad= ?1")
    List<Transito> buscarByLocalidad(String localidad);

    @Query("from Transito t where t.persona.dni= ?1")
    Optional<Transito> findByDni(String dni);

    @Query("from Transito t where t.persona.email= ?1")
    Optional<Transito> findByEmail(String email);

}
