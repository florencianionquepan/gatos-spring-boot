package com.example.gatosspringboot.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class LoggingFilter extends OncePerRequestFilter {

    private Logger logger= LoggerFactory.getLogger(CsrfCookieFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        response.getHeaderNames().forEach(n->
                logger.info("Header {}: {}", n, response.getHeader(n)));
        chain.doFilter(request, response);
    }
}
