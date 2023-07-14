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
@Table(name="voluntarios")
@PrimaryKeyJoinColumn(referencedColumnName = "id")
@Getter
@Setter
@NoArgsConstructor
public class Voluntario extends Persona implements Serializable {
    @OneToMany(mappedBy = "voluntario", fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="voluntario")
    private List<Gato> listaGatos;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="us_id")
    private Usuario usuario;

    public Voluntario(Long id, String dni, String nombre, String apellido, String tel,
                      String email, LocalDate fechaNac, String dire, String localidad,
                      List<Solicitud> solicitudesAdopcion,
                      List<SolicitudVoluntariado> solicitudesVoluntariados,
                      List<Gato> listaGatos, Usuario usuario) {
        super(id, dni, nombre, apellido, tel, email, fechaNac, dire, localidad, solicitudesAdopcion, solicitudesVoluntariados);
        this.listaGatos = listaGatos;
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Voluntario{" +
                super.toString() +
                "listaGatos=" + listaGatos +
                ", usuario=" + usuario +
                '}';
    }
}
