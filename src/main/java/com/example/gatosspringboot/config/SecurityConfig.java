package com.example.gatosspringboot.config;

import com.example.gatosspringboot.filter.*;
import com.example.gatosspringboot.repository.database.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class SecurityConfig {

    public final UsuarioRepository repo;

    public SecurityConfig(UsuarioRepository repo) {
        this.repo = repo;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        CsrfTokenRequestAttributeHandler requestHandler= new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf");

        http.sessionManagement(httpSecuritySessionManagementConfigurer -> {
                    httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .cors((cors)->cors.configurationSource(new CorsConfigurationSource() {
                    //anonymous inner class
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config=new CorsConfiguration();
                        config.setAllowedOrigins(Arrays.asList("http://localhost:4200/","http://127.0.0.1:8080/"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setExposedHeaders(Arrays.asList("Authorization"));
                        config.setMaxAge(3600L);
                        return config;
                    }
                }))
                .csrf((csrf) -> csrf.csrfTokenRequestHandler(requestHandler).ignoringRequestMatchers("/gatos/**","/personas/**","/cuotas/**",
                                "/generic/**","/usuarios/**","/cloudinary/**","/ficha/**","/solicitudes/**","/notificaciones/**","/padrinos/**","/voluntariados/**")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                //.addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new JWTGenerationFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new JWTValidationFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new EmailValidationFilter(repo), JWTGenerationFilter.class)
                .addFilterAfter(new EmailNonExistingFilter(repo), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((requests)->requests
                        .requestMatchers("/socios/**","/voluntariados/**","/voluntariados","/voluntarios/**",
                                "/solicitudes/**","/auth/**","/notificaciones/**","/cuotas/**","/padrinos/**","/transitos/**").authenticated()
                        .requestMatchers("/gatos/**", "/usuarios/**","/personas/**","/cloudinary/**").permitAll())
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
