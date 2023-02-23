package com.example.gatosspringboot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name="aspirantes")
@PrimaryKeyJoinColumn(referencedColumnName = "dni")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Aspirante extends Persona implements Serializable {
    private LocalDate fechaSol;
    private String tipoVoluntariado;
    @ManyToOne
    @JoinColumn(name="estado_id")
    private Estado estado;
}
