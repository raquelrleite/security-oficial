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
import java.time.temporal.ChronoUnit;

import static br.com.securityoficial.enums.ErrorCode.TOKEN_GENERATION_ERROR;
import static br.com.securityoficial.enums.ErrorCode.TOKEN_INVALID_OR_EXPIRED;

@Service
public class TokenService {

    private static final String ISSUER = "security-oficial";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-hours}")
    private Long expirationHours;

    @Value("${jwt.refresh-expiration-days}")
    private long refreshExpirationDays;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);
            return JWT.create()
                    .withIssuer(ISSUER) // Identificador da aplicação
                    .withSubject(user.getUsername()) // Recomendado usar o email ou username
                    .withExpiresAt(generateExpirationDate(expirationHours, ChronoUnit.HOURS))
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new BusinessException(TOKEN_GENERATION_ERROR);
        }
    }

    public String refreshToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(user.getId().toString())
                    .withExpiresAt(generateExpirationDate(refreshExpirationDays, ChronoUnit.DAYS))
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new BusinessException(TOKEN_GENERATION_ERROR);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);
            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            throw new BusinessException(TOKEN_INVALID_OR_EXPIRED, e);
        }
    }

    private Instant generateExpirationDate(long time, ChronoUnit unit) {
        return Instant.now().plus(time, unit);
    }
}