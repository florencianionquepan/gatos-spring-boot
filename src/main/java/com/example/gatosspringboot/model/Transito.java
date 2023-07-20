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
@Table(name="transitos")
@PrimaryKeyJoinColumn(referencedColumnName = "id")
@Getter
@Setter
@NoArgsConstructor
public class Transito extends Persona implements Serializable {
    @OneToMany(mappedBy = "transito", fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="transito")
    private List<Gato> listaGatos;

    public Transito(Long id, String dni, String nombre, String apellido, String tel,
                    String email, LocalDate fechaNac, String dire, String localidad,
                    List<Solicitud> solicitudesAdopcion,
                    List<SolicitudVoluntariado> solicitudesVoluntariados,
                    Usuario usuario, List<Gato> listaGatos) {
        super(id, dni, nombre, apellido, tel, email, fechaNac, dire,
                localidad, solicitudesAdopcion, solicitudesVoluntariados, usuario);
        this.listaGatos = listaGatos;
    }
}
