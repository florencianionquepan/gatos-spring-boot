package com.example.gatosspringboot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

    @NotNull
    private LocalDate fechaSol;
    @NotNull
    private String tipoVoluntariado;
    @ManyToOne
    @JoinColumn(name="estado_id")
    private Estado estado;
}
