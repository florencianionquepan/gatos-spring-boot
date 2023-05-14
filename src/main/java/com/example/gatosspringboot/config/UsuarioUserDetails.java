package com.example.gatosspringboot.config;

import com.example.gatosspringboot.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

//crea un UsuarioUserDetails a partir de un Usuario
public class UsuarioUserDetails implements UserDetails {

    private String email;
    private String contrasenia;
    private List<GrantedAuthority> authorities;

    public UsuarioUserDetails(Usuario user) {
        email=user.getMail();
        contrasenia=user.getContrasenia();
        authorities= user.getRoles().stream()
                .map(role->new SimpleGrantedAuthority(role.getNombre()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return contrasenia;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
