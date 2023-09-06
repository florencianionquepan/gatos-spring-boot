package com.example.gatosspringboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="voluntarios")
@Getter
@Setter
@NoArgsConstructor
public class Voluntario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @NotNull
    private Persona persona;

    @OneToMany(mappedBy = "voluntario", fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="voluntario")
    private List<Gato> listaGatos;

    @Override
    public String toString() {
        return "Voluntario{" +
                super.toString() +
                "listaGatos=" + listaGatos +
                '}';
    }
}
