package com.example.gatosspringboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="solicitudes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudAdopcion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name="solicitante_dni")
    @NotNull
    @JsonIgnoreProperties(value="solicitudesAdopcion")
    private Persona solicitante;

    @ManyToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name="gato_id")
    @NotNull
    @JsonIgnoreProperties(value="solicitudesAdopcion")
    private Gato gato;

    @ManyToMany(cascade=CascadeType.MERGE)
    @JoinTable(
            name="solicitud_adopcion_estado",
            joinColumns = @JoinColumn(name="solicitud_id"),
            inverseJoinColumns = @JoinColumn(name="estado_id")
    )
    @NotNull
    private List<Estado> estados;

    @Override
    public String toString() {
        return "Solicitud{" +
                "id=" + id +
                ", solicitante=" + solicitante +
                ", gato=" + gato +
                ", estados=" + estados +
                '}';
    }
}
