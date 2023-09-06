package com.example.gatosspringboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="socios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Socio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @NotNull
    private Persona persona;

    @OneToMany(mappedBy = "socio", cascade = CascadeType.MERGE)
    @JsonIgnoreProperties(value="socio")
    private List<SolicitudVoluntariado> solicitudesVoluntariados;

}
