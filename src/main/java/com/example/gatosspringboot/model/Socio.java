package com.example.gatosspringboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="socios")
@PrimaryKeyJoinColumn(referencedColumnName = "id")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Socio extends Persona implements Serializable {

    @OneToMany(mappedBy = "socio", cascade = CascadeType.MERGE)
    @JsonIgnoreProperties(value="socio")
    private List<SolicitudVoluntariado> solicitudesVoluntariados;

    @OneToOne
    @JoinColumn(name="us_id")
    private Usuario usuario;

}
