package com.example.gatosspringboot.repository.database;

import com.example.gatosspringboot.model.Padrino;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PadrinoRepository extends CrudRepository<Padrino,Long> {
    @Query("from Padrino p where p.email= ?1")
    Optional<Padrino> buscarByEmail(String email);

    @Modifying
    @Query(nativeQuery = true,
            value="INSERT INTO padrinos (id) VALUES (:id)")
    void savePadrino(@Param("id") Long id);
}
