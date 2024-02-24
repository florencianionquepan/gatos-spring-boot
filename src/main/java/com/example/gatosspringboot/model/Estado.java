package com.example.gatosspringboot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name="estados")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Estado implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private LocalDate fecha;
    @NotNull
    private EstadoNombre estado;
    private String motivo;

    @Override
    public String toString() {
        return "Estado{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", estado=" + estado +
                ", motivo='" + motivo + '\'' +
                '}';
    }
}