
package com.taskflow.taskflow.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.taskflow.taskflow.repository.UserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

import com.taskflow.taskflow.exception.ResourceNotFoundException;
@Configuration
public class SecurityConfig {

    // con @bean decimos Spring, ejecuta este método, obtén un BCryptPasswordEncoder 
    // y administra ese objeto como una dependencia de tipo PasswordEncoder
    // entonces Spring, cuando ve un @Bean, construye objeto y lo guarda en su contenedor
    // de dependencias, y lo inyecta él solito donde se necesite
    // passwordEncoder es como un constructor, pero de objetos externos -> por eso tiene un tipo de retorno
    @Bean
    // paswordEncoder es una interfaz de Spring Security para trabajar con contraseñas
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCryptPasswordEncoder: es una implementación de pass..Encoder que utiliza BCrypt para generar y verificar hashes.
    }

    @Bean
    // UserDetailsService es una interfaz de Spring Security que permite cargar los datos de un usuario a partir de su nombre de usuario
    // acá lo que hacemos es implementarla, devolviendo objeto de ese tipo
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        // El método userDetailsService() devuelve un objeto que implementa UserDetailsService.
        return email -> userRepository.findByEmail(email) // obtenemos user usando email -> declarado en repository e implementado por spring data JPA
                .map(user -> User.withUsername(user.getEmail()) // obtiene user usando username (en nuestro caso, email)
                        .password(user.getPasswordHash()) // para cada user obtenido, se setea passw, y roles
                        .roles("USER")
                        .build()) // construimos objeto details, que contendrá lo necesario para autenticar, diferente a nuestro User
                .orElseThrow(() ->
                        new UsernameNotFoundException("Invalid credentials")
                );
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtEncoder jwtEncoder(
            @Value("${JWT_SECRET}") String secret
    ) {
        SecretKey key = createSecretKey(secret);
        return new NimbusJwtEncoder(new ImmutableSecret<>(key));
    }

    @Bean
    public JwtDecoder jwtDecoder(
            @Value("${JWT_SECRET}") String secret
    ) {
        SecretKey key = createSecretKey(secret);

        return NimbusJwtDecoder
                .withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    private SecretKey createSecretKey(String secret) {
        byte[] decodedKey = Base64.getDecoder().decode(secret);

        return new SecretKeySpec(
                decodedKey,
                "HmacSHA256"
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session ->
                    session.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS
                    )
            )
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/auth/**").permitAll()
                    .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 ->
                    oauth2.jwt(jwt -> {})
            )
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
