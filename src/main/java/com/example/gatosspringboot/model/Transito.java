package com.example.gatosspringboot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="transitos")
@PrimaryKeyJoinColumn(referencedColumnName = "dni")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transito extends Persona implements Serializable {
    @OneToMany(mappedBy = "transito", fetch = FetchType.LAZY)
    private List<Gato> listaGatos;
}
