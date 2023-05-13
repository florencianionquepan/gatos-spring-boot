package com.example.gatosspringboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="transitos")
@PrimaryKeyJoinColumn(referencedColumnName = "id")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transito extends Persona implements Serializable {
    @OneToMany(mappedBy = "transito", fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="transito")
    private List<Gato> listaGatos;
}
