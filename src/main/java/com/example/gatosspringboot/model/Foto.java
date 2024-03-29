package com.example.gatosspringboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="fotos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Foto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String fotoUrl;
    private String fotoId;

    @ManyToOne
    @JsonIgnoreProperties(value="fotos")
    private Gato gato;

    @Override
    public String toString() {
        return "Foto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", fotoUrl='" + fotoUrl + '\'' +
                ", fotoId='" + fotoId + '\'' +
                '}';
    }
}
