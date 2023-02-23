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
@Inheritance(strategy= InheritanceType.JOINED)
@Table(name="personas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Persona implements Serializable {
    @Id
    private String dni;
    private String nombre;
    private String apellido;
    private String tel;
    private String email;
    private LocalDate fechaNac;
    private String dire;
    private String localidad;
    @OneToMany(mappedBy = "solicitante", fetch = FetchType.LAZY)
    private List<Solicitud> listaSol;

}
