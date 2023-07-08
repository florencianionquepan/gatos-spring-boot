package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.model.Aspirante;
import com.example.gatosspringboot.model.Voluntario;
import org.mapstruct.*;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
public interface IAspiranteVoluntarioMapper {

    @InheritInverseConfiguration
    Voluntario mapAspiranteToVoluntario(Aspirante aspi);

    @AfterMapping
    default void setNullId(@MappingTarget Voluntario voluntario) {
        voluntario.setId(null);
    }
}
