package br.com.securityoficial.infra.security;


import br.com.securityoficial.entity.User;
import br.com.securityoficial.exception.BusinessException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static br.com.securityoficial.enums.ErrorCode.TOKEN_GENERATION_ERROR;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-hours}")
    private Long expirationHours;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);
            return JWT.create()
                    .withIssuer("security-oficial") // Identificador da aplicação
                    .withSubject(user.getUsername()) // Recomendado usar o email ou username
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new BusinessException(TOKEN_GENERATION_ERROR);
        }
    }
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);
            return JWT.require(algorithm)
                    .withIssuer("security-oficial")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now()
                .plusHours(expirationHours)
                .toInstant(ZoneOffset.of("-03:00"));
    }
}