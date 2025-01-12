package com.example.taskmanager.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;


@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {


    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!this.isHeaderValid(authHeader)) {
            filterChain.doFilter(request, response);
            return;
        }
        jwtService.verifyAccessToken(authHeader.substring(7))
                .ifPresent(decodedJWT -> {
                    var username = decodedJWT.getSubject();
                    var authToken = new UsernamePasswordAuthenticationToken(
                            username, null, Collections.emptyList()
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                });
        filterChain.doFilter(request, response);
    }

    private boolean isHeaderValid(String authHeader) {
        return authHeader != null && authHeader.startsWith("Bearer ");
    }
}
