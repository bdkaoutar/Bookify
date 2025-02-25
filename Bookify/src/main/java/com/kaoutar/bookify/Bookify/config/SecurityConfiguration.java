package com.kaoutar.bookify.Bookify.config;

import com.kaoutar.bookify.Bookify.model.Role;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(
            AuthenticationProvider authenticationProvider,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/auth/**").permitAll() // Public routes
                        .requestMatchers("/api/v1/books/publish/**").hasAuthority("ROLE_" + Role.AUTHOR.name())
                        .anyRequest().authenticated() // All other routes require authentication
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter

//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/api/v1/auth/**").permitAll()  // Public routes for authentication
//                        .requestMatchers("/api/v1/books/**").permitAll() // Allow all users to browse books
//                        .requestMatchers("/api/v1/users/**").hasAnyRole("USER", "AUTHOR") // Readers & Authors can access user features
//                        .requestMatchers("/api/v1/books/{bookId}/review").authenticated() // Only authenticated users can review books
//                        .anyRequest().authenticated()
//                )
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authenticationProvider(authenticationProvider)
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // Allow all origins
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public FilterRegistrationBean<RequestLoggingFilter> loggingFilter() {
        FilterRegistrationBean<RequestLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestLoggingFilter());
        return registrationBean;
    }

    public static class RequestLoggingFilter extends OncePerRequestFilter implements Filter {
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("Request: " + request.getRequestURI());
            System.out.println("Authenticated User: " + (auth != null ? auth.getName() : "None"));
            System.out.println("Authorities: " + (auth != null ? auth.getAuthorities() : "None"));
            filterChain.doFilter(request, response);
        }
    }

}
