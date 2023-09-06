package com.example.gatosspringboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="padrinos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Padrino implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @NotNull
    private Persona persona;

    @OneToMany(mappedBy = "padrino", fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value={"padrino","solicitudesAdopcion"})
    private List<Gato> listaGatos;

    @OneToMany(mappedBy = "padrino", fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="padrino")
    private List<Cuota> listaCuotas;

}
