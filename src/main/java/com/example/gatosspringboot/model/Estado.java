package com.example.gatosspringboot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="estados")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Estado implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private String estado;
    @OneToMany(mappedBy = "estado")
    private Solicitud solicitud;

    @OneToMany(mappedBy = "estado")
    private Aspirante aspirante;
}
