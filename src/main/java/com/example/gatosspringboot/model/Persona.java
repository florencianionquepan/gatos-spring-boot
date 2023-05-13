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
    @Column(nullable = false, length = 20)
    private String email;
    @Column(nullable = false)
    private LocalDate fechaNac;
    @Column(nullable = false, length = 30)
    private String dire;
    @Column(nullable = false, length = 30)
    private String localidad;
    @OneToMany(mappedBy = "solicitante", fetch = FetchType.LAZY)
    private List<Solicitud> solicitudes;
}
