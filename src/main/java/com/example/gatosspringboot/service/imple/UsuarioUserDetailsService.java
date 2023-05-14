package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.config.UsuarioUserDetails;
import com.example.gatosspringboot.model.Usuario;
import com.example.gatosspringboot.repository.database.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

//Bueca un usuario segun email y lo convierte en UserDetails
public class UsuarioUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repo;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Usuario> oUsuario=repo.findByEmail(email);
        return oUsuario.map(UsuarioUserDetails::new)
                .orElseThrow(()->new UsernameNotFoundException("Usuario no encontrado"));
    }
}
