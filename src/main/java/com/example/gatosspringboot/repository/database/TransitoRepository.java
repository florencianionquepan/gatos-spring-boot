package com.example.gatosspringboot.repository.database;

import com.example.gatosspringboot.model.Transito;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TransitoRepository extends CrudRepository<Transito,Long> {

    @Query("from Transito t where t.localidad= ?1")
    List<Transito> buscarByLocalidad(String localidad);

    @Modifying
    @Query(nativeQuery = true,
            value="INSERT INTO transitos (id) VALUES (:id)")
    void saveTransito(@Param("id") Long id);

    @Query("from Transito t where t.dni= ?1")
    Optional<Transito> findByDni(String dni);

    @Query("from Transito t where t.email= ?1")
    Optional<Transito> findByEmail(String email);

}
