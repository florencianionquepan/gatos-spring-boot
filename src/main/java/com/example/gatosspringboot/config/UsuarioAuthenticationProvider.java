package com.example.gatosspringboot.config;

import com.example.gatosspringboot.controller.PersonaController;
import com.example.gatosspringboot.model.Rol;
import com.example.gatosspringboot.model.Usuario;
import com.example.gatosspringboot.repository.database.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UsuarioAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UsuarioRepository repo;
    @Autowired
    private PasswordEncoder encoder;
    private Logger logger= LoggerFactory.getLogger(UsuarioAuthenticationProvider.class);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //logger.info("auth en provider:"+authentication);
        String email= authentication.getName();
        String pwd=authentication.getCredentials().toString();
        Optional<Usuario> oUser= repo.findByEmail(email);
        if(oUser.isPresent()){
            if(encoder.matches(pwd,oUser.get().getContrasenia())){
                List<String> roles=oUser.get().getRoles().stream()
                        .map(Rol::getNombre).collect(Collectors.toList());
                List<SimpleGrantedAuthority> authorities=new ArrayList<>();
                roles.forEach(role->authorities.add(new SimpleGrantedAuthority(role)));
                return new UsernamePasswordAuthenticationToken(email, pwd, authorities);
            }else{
                throw new BadCredentialsException("Invalid password!");
            }
        }else{
            throw new BadCredentialsException("No user registered with this details!");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
