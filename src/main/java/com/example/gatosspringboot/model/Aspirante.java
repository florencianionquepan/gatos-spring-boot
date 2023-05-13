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
@Table(name="aspirantes")
@PrimaryKeyJoinColumn(referencedColumnName = "id")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Aspirante extends Persona implements Serializable {

    @NotNull
    @ElementCollection(targetClass = TipoVoluntariado.class)
    @Enumerated(EnumType.STRING)
    private List<TipoVoluntariado> tiposVoluntariado;

    @ManyToMany(cascade=CascadeType.MERGE)
    @JoinTable(
            name="aspirante_estado",
            joinColumns = @JoinColumn(name="aspirante_id"),
            inverseJoinColumns = @JoinColumn(name="estado_id")
    )

    @NotNull
    private List<Estado> estados;

    @ManyToOne
    @JsonIgnoreProperties(value="aspirantes")
    private Socio socio;
}
