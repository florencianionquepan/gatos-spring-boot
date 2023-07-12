package com.example.gatosspringboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="solicitudesvoluntariados")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudVoluntariado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    @ManyToOne(cascade = CascadeType.MERGE)
    @JsonIgnoreProperties(value="solicitudesVoluntariados")
    private Persona aspirante;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoVoluntariado tipoVoluntariado;

    @ManyToMany(cascade= CascadeType.MERGE)
    @JoinTable(
            name="solicitud_voluntariado_estado",
            joinColumns = @JoinColumn(name="voluntariado_id"),
            inverseJoinColumns = @JoinColumn(name="estado_id")
    )
    @NotNull
    private List<Estado> estados;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JsonIgnoreProperties(value="solicitudesVoluntariados")
    private Socio socio;

}
