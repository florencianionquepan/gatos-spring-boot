package com.example.gatosspringboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="padrinos")
@PrimaryKeyJoinColumn(referencedColumnName = "id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Padrino extends Persona implements Serializable {

    @OneToMany(mappedBy = "padrino", fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value={"padrino","solicitudesAdopcion"})
    private List<Gato> listaGatos;

    @OneToMany(mappedBy = "padrino", fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="padrino")
    private List<Cuota> listaCuotas;

}
