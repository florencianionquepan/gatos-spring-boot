package com.example.gatosspringboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name="notificaciones")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String descripcion;
    @NotNull
    private LocalDate fechaCreacion;
    @NotNull
    private Boolean leida=false;
    @ManyToOne(cascade=CascadeType.MERGE)
    @NotNull
    @JsonIgnoreProperties(value={"notificaciones","solicitudesAdopcion","solicitudesVoluntariados","usuario"})
    private Persona persona;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDate.now();
    }
}
