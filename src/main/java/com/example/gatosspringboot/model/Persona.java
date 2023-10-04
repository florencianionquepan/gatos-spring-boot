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
@Table(name="personas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Persona implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, unique = true)
    private String dni;
    @Column(nullable = false, length = 15)
    private String nombre;
    @Column(nullable = false, length = 15)
    private String apellido;
    @Column(nullable = false, length = 30)
    private String tel;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private LocalDate fechaNac;
    @Column(nullable = false, length = 50)
    private String dire;
    @Column(nullable = false, length = 50)
    private String localidad;

    @OneToMany(mappedBy = "solicitante", cascade = CascadeType.MERGE)
    @JsonIgnoreProperties(value="solicitante")
    private List<SolicitudAdopcion> solicitudesAdopcion;

    @OneToMany(mappedBy = "aspirante", cascade = CascadeType.MERGE)
    @JsonIgnoreProperties(value="aspirante")
    private List<SolicitudVoluntariado> solicitudesVoluntariados;

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "persona")
    @JsonIgnoreProperties(value="persona")
    private List<Notificacion> notificaciones;

    @OneToOne
    @JoinColumn(name="us_id")
    private Usuario usuario;

    @Override
    public String toString() {
        return "Persona{" +
                "id=" + id +
                ", dni='" + dni + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", tel='" + tel + '\'' +
                ", fechaNac=" + fechaNac +
                ", dire='" + dire + '\'' +
                ", localidad='" + localidad + '\'' +
                '}';
    }
}
