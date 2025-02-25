package com.kaoutar.bookify.Bookify.config;

import com.kaoutar.bookify.Bookify.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            HandlerExceptionResolver handlerExceptionResolver,
            JwtService jwtService,
            UserDetailsService userDetailsService
    ) {
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + authHeader);

        // Skip JWT token validation for login and signup routes
        if (request.getRequestURI().startsWith("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;  // Skip the filter for login/signup routes
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("No Bearer token found, skipping authentication.");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            final String userUsername = jwtService.extractUsername(jwt);
            System.out.println("Extracted Username from Token: " + userUsername);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (userUsername != null && authentication == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userUsername);
                System.out.println("Loaded UserDetails: " + userDetails.getUsername());

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    System.out.println("Token is valid. Setting Authentication...");
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("SecurityContext updated with user: " + userDetails.getUsername());
                } else {
                    System.out.println("Token is invalid.");
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception in JwtAuthenticationFilter: " + e.getMessage());
            filterChain.doFilter(request, response);
        }
    }

}
