package com.example.gatosspringboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="usuarios")
@Getter
@Setter
@AllArgsConstructor
public class Usuario implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @NotNull
    private String contrasenia;
    @Column(columnDefinition = "boolean default false")
    private Boolean validado;
    @Column(columnDefinition = "boolean default true")
    private Boolean habilitado;
    private String motivo;

    @ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinTable(
            name="roles_usuarios",
            joinColumns = @JoinColumn(name="usuario_id"),
            inverseJoinColumns = @JoinColumn (name="rol_id")
    )
    @JsonIgnoreProperties(value="usuarios")
    private List<Rol> roles;

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", mail='" + email + '\'' +
                ", roles=" + roles +
                '}';
    }

    public Usuario() {
        this.validado = false;
        this.habilitado = true;
    }
}
