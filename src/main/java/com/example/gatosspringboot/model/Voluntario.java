package com.example.gatosspringboot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="voluntarios")
@PrimaryKeyJoinColumn(referencedColumnName = "dni")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Voluntario extends Persona implements Serializable {
    @OneToMany(mappedBy = "voluntario", fetch = FetchType.LAZY)
    private List<Gato> listaGatos;

    @OneToOne
    @JoinColumn(name="us_id")
    private Usuario usuario;

}
