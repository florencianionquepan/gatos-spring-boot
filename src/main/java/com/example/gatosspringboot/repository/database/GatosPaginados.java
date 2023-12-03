package com.example.gatosspringboot.repository.database;

import com.example.gatosspringboot.model.Gato;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface GatosPaginados extends PagingAndSortingRepository<Gato,Long> {

    @Query("SELECT g FROM Gato g ORDER BY CASE WHEN g.adoptadoFecha IS NULL THEN 0 ELSE 1 END, " +
            "CASE WHEN g.adoptadoFecha IS NOT NULL THEN g.id END DESC, g.id DESC")
    Page<Gato> findAllOrdered(Pageable pageable);
}

