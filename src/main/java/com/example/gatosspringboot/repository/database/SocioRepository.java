package com.example.gatosspringboot.repository.database;

import com.example.gatosspringboot.model.Socio;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SocioRepository extends CrudRepository<Socio,Long> {
    @Query("from Socio s where s.email= ?1")
    Optional<Socio> findByEmail(String email);

    @Query("from Socio s where s.dni= ?1")
    Optional<Socio> findByDni(String dni);

    @Modifying
    @Query(nativeQuery = true,
            value="INSERT INTO socios (id, us_id) VALUES (:id, :us_id)")
    void saveSocio(@Param("id") Long id, @Param("us_id") Long us_id);
}
