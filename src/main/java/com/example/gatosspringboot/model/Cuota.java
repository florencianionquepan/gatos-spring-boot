package com.example.gatosspringboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate fechaPago;
    private double montoMensual;

    @ManyToOne
    @JoinColumn(name="padrino_dni")
    @JsonIgnoreProperties(value="listaCuotas")
    private Padrino padrino;

    @ManyToOne
    @JoinColumn(name = "gato_id")
    @JsonIgnoreProperties(value={"padrino","solicitudesAdopcion","cuotas"})
    private Gato gato;
}
