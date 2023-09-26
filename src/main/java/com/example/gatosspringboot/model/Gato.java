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
    @OneToMany(mappedBy = "gato",fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JsonIgnoreProperties(value="gato")
    private List<Foto> fotos;
    @Column(nullable = false, length = 10)
    private String edad;
    @Column(nullable = false, length = 10)
    private String sexo;
    private String descripcion;
    @Column(nullable = false, length = 15)
    private String color;
    @Column(nullable = false, length = 15)
    private String tipoPelo;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "ficha_vet")
    private Ficha fichaVet;

    @OneToMany(mappedBy = "gato", fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JsonIgnoreProperties(value="gato")
    private List<SolicitudAdopcion> solicitudesAdopcion;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JsonIgnoreProperties(value="listaGatos")
    private Voluntario voluntario;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JsonIgnoreProperties(value="listaGatos")
    private Transito transito;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JsonIgnoreProperties(value="listaGatos")
    private Padrino padrino;

    private LocalDate adoptadoFecha;

    private double montoMensual;

    @OneToMany(mappedBy = "gato", fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JsonIgnoreProperties(value="gato")
    private List<Cuota> cuotas;

    @Override
    public String toString() {
        return "Gato{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", adoptadoFecha=" + adoptadoFecha +
                ", fotos="+ fotos +
                '}';
    }
}
