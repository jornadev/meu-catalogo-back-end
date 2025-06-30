package com.uri.meucatalogo.config;

import com.uri.meucatalogo.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity // Habilita @PreAuthorize e outras anotações de segurança em métodos
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> {})
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
            .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
            .requestMatchers("/auth/login").permitAll()
            .requestMatchers("/auth/register").permitAll()
            .requestMatchers("/genres/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/movies/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/movies/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/movies/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/movies/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.POST, "/movies/*/reviews").authenticated()
            .requestMatchers(HttpMethod.GET, "/usuarios/*/avaliacoes").authenticated() // 👈 ADICIONE ISSO
            .requestMatchers(HttpMethod.POST, "/reviews/*/comments").authenticated()
            .requestMatchers(HttpMethod.GET, "/reviews/movie/**").permitAll() // Liberando o acesso público
            .requestMatchers(HttpMethod.GET, "/generos").permitAll() // Liberando o acesso público à lista de gêneros
            .anyRequest().authenticated()
        )

            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
