package com.example.gatosspringboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 15)
    private String nombre;
    @ElementCollection(targetClass=String.class)
    private List<String> srcFoto;
    @Column(nullable = false, length = 10)
    private String edad;
    @Column(nullable = false, length = 10)
    private String sexo;
    private String descripcion;
    @Column(nullable = false, length = 15)
    private String color;
    @Column(nullable = false, length = 15)
    private String tipoPelo;
    @OneToOne
    @JoinColumn(name = "ficha_vet")
    private Ficha fichaVet;

    @OneToMany(mappedBy = "gato", fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JsonIgnoreProperties(value="gato")
    private List<Solicitud> listaSol;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="volunt_dni")
    @JsonIgnoreProperties(value="listaGatos")
    private Voluntario voluntario;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="transito_dni")
    @JsonIgnoreProperties(value="listaGatos")
    private Transito transito;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="padrino_dni")
    @JsonIgnoreProperties(value="listaGatos")
    private Padrino padrino;

    private LocalDate adoptadoFecha;

}
