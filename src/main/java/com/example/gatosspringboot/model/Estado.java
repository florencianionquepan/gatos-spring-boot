package com.example.gatosspringboot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

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
    private LocalDate fecha;
    private EstadoNombre estado;
    @ManyToMany(mappedBy = "estado")
    private List<Solicitud> listaSolicitudes;

    @ManyToMany(mappedBy = "estado")
    private List<Aspirante> listaAspirantes;
}
