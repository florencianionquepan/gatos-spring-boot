package com.example.gatosspringboot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name="cuotas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cuota implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private LocalDate fechaPago;
    private double montoMensual;
    @ManyToOne
    @JoinColumn(name="padrino_dni")
    private Padrino padrino;
}
