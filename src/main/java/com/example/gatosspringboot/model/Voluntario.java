package com.example.gatosspringboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    public Voluntario(Long id, String dni, String nombre, String apellido, String tel,
                      String email, LocalDate fechaNac, String dire, String localidad,
                      List<Solicitud> solicitudesAdopcion,
                      List<SolicitudVoluntariado> solicitudesVoluntariados,
                      Usuario usuario, List<Gato> listaGatos) {
        super(id, dni, nombre, apellido, tel, email, fechaNac, dire,
                localidad, solicitudesAdopcion, solicitudesVoluntariados, usuario);
        this.listaGatos = listaGatos;
    }

    @Override
    public String toString() {
        return "Voluntario{" +
                super.toString() +
                "listaGatos=" + listaGatos +
                '}';
    }
}
