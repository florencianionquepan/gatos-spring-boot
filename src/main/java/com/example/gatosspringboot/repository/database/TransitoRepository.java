package com.example.gatosspringboot.repository.database;

import com.example.gatosspringboot.model.Transito;
import com.example.gatosspringboot.model.Voluntario;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransitoRepository extends CrudRepository<Transito,Long> {

    @Query("from Transito t where t.localidad= ?1")
    List<Transito> buscarByLocalidad(String localidad);

    @Modifying
    @Query(nativeQuery = true,
            value="INSERT INTO transitos (id) VALUES (:id)")
    void saveTransito(@Param("id") Long id);
}
