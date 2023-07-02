package com.example.gatosspringboot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Inheritance(strategy= InheritanceType.JOINED)
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
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private LocalDate fechaNac;
    @Column(nullable = false, length = 30)
    private String dire;
    @Column(nullable = false, length = 30)
    private String localidad;
    @OneToMany(mappedBy = "solicitante", fetch = FetchType.LAZY)
    private List<Solicitud> solicitudes;

    @Override
    public String toString() {
        return "Persona{" +
                "id=" + id +
                ", dni='" + dni + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", tel='" + tel + '\'' +
                ", email='" + email + '\'' +
                ", fechaNac=" + fechaNac +
                ", dire='" + dire + '\'' +
                ", localidad='" + localidad + '\'' +
                ", solicitudes=" + solicitudes +
                '}';
    }
}
