package com.example.gatosspringboot.filter;

import com.example.gatosspringboot.config.UsuarioAuthenticationProvider;
import com.example.gatosspringboot.model.Usuario;
import com.example.gatosspringboot.repository.database.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

public class EmailValidationFilter extends OncePerRequestFilter {

    private final UsuarioRepository repo;

    private Logger logger= LoggerFactory.getLogger(UsuarioAuthenticationProvider.class);

    public EmailValidationFilter(UsuarioRepository repo) {
        this.repo = repo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null){
            String email = authentication.getName();
            Optional<Usuario> oUser= this.repo.findByEmail(email);
            if(oUser.isPresent() && !oUser.get().getValidado()){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                String jsonResponse = "{\"message\": \"" + "Debe validar su email para iniciar sesión" +
                        "\", \"key\": \"" + "invalido" + "\"}";
                response.getWriter().write(jsonResponse);
                response.getWriter().flush();
                return;
            }if(oUser.isPresent() && oUser.get().getHabilitado()!=null ){
                if(!oUser.get().getHabilitado()) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    String jsonResponse = "{\"message\": \"" + "Su cuenta se encuentra bloqueada" +
                            "\", \"key\": \"" + "bloqueado" + "\"}";
                    response.getWriter().write(jsonResponse);
                    response.getWriter().flush();
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
