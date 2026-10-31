package com.taskflow.taskflow.security;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import com.taskflow.taskflow.model.User;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;

    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(User user) {

        Instant now = Instant.now();

        // construimos la información que contendrá el token -> los claims
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("taskflow") //iss
                .issuedAt(now) // iat
                .expiresAt(now.plus(1, ChronoUnit.HOURS)) // exp
                .subject(user.getId().toString()) // sub
                .build(); // construye objetos con ese claim

        // generamos cabecera de JWT -> Recordemos que este tiene 3 partes
        // header.payload.signature 
        // El header contiene metadatos sobre cómo se construyó y firmó el token.
        // signature se genera con un secret y el resto de JWT codificado
        JwsHeader header = JwsHeader
                .with(MacAlgorithm.HS256) // algoritmo con el que se firmó JWT
                .type("JWT")
                .build();

        // Generamos token
        // agrupa info necesaria para hacerlo: Header -> cómo se creará
        // Claims -> info que usará
        // encode genera el token -> 
        // 1. Construye el JWT con su header y payload.
        // 2. Codifica ambas partes utilizando Base64URL.
        // 3. Utiliza la clave secreta configurada para generar la firma HS256.
        // 4. Construye el token completo.
        // NOTA: La clave secreta se define en jwtEncoder
        return jwtEncoder.encode(
                JwtEncoderParameters.from(header, claims)
        ).getTokenValue();
    }
}