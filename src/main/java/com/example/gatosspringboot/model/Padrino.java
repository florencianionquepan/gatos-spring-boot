package com.example.gatosspringboot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="padrinos")
@PrimaryKeyJoinColumn(referencedColumnName = "id")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Padrino extends Persona implements Serializable {

    @OneToMany(mappedBy = "padrino", fetch = FetchType.LAZY)
    private List<Gato> listaGatos;

    @OneToMany(mappedBy = "padrino", fetch = FetchType.LAZY)
    private List<Cuota> listaCuotas;

}
