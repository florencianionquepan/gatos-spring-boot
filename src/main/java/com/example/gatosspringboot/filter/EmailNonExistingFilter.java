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
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

public class EmailNonExistingFilter extends OncePerRequestFilter {

    private final UsuarioRepository repo;

    public EmailNonExistingFilter(UsuarioRepository repo) {
        this.repo = repo;
    }

    private Logger logger= LoggerFactory.getLogger(EmailNonExistingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader=request.getHeader("Authorization");
        String base64Credentials = authHeader.substring("Basic".length()).trim();
        byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
        String decodedCredentials = new String(decodedBytes, StandardCharsets.UTF_8);
        String username=decodedCredentials.split(":",2)[0];
        if(username!=null) {
            Optional<Usuario> oUser = this.repo.findByEmail(username);
            if (oUser.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                String jsonResponse = "{\"message\": \"" + "Debe registrarse para iniciar sesión" +
                        "\", \"key\": \"" + "inexistente" + "\"}";
                response.getWriter().write(jsonResponse);
                response.getWriter().flush();
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().equals("/auth");
    }
}
