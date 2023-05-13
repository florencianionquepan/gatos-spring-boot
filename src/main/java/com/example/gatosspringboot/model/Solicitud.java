package com.example.gatosspringboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="solicitudes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Solicitud implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private LocalDate fechaSolicitud;

    @ManyToOne
    @JoinColumn(name="solicitante_dni")
    @NotNull
    private Persona solicitante;

    @ManyToOne
    @JoinColumn(name="gato_id")
    @NotNull
    private Gato gato;

    @ManyToMany(cascade=CascadeType.MERGE)
    @JoinTable(
            name="solicitud_estado",
            joinColumns = @JoinColumn(name="solicitud_id"),
            inverseJoinColumns = @JoinColumn(name="estado_id")
    )

    @NotNull
    private List<Estado> estados;
}
