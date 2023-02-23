package com.example.gatosspringboot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

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
    private LocalDate fechaSol;

    @ManyToOne
    @JoinColumn(name="solicitante_dni")
    private Persona solicitante;

    @ManyToOne
    @JoinColumn(name="gato_id")
    private Gato gato;

    @ManyToOne
    @JoinColumn(name="estado_id")
    private Estado estado;
}
