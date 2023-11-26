package com.example.gatosspringboot.repository.database;

import com.example.gatosspringboot.model.Gato;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface GatosPaginados extends PagingAndSortingRepository<Gato,Long> {
}

