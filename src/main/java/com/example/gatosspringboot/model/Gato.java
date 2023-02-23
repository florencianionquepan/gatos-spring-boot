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
@Table(name="gatos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Gato implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String nombre;

    @ElementCollection(targetClass=String.class)
    private List<String> srcFoto;
    private String edad;
    private String sexo;
    private String descripcion;
    private String raza;
    private String color;
    private String tipoPelo;
    @OneToOne
    @JoinColumn(name = "ficha_vet")
    private Ficha fichaVet;

    @OneToMany(mappedBy = "gato", fetch = FetchType.LAZY)
    private List<Solicitud> listaSol;

    @ManyToOne()
    @JoinColumn(name="volunt_dni")
    private Voluntario voluntario;

    @ManyToOne()
    @JoinColumn(name="transito_dni")
    private Transito transito;

    @ManyToOne()
    @JoinColumn(name="padrino_dni")
    private Padrino padrino;

    private LocalDate adoptado;

}
