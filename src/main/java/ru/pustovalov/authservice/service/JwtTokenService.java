package ru.pustovalov.authservice.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JwtTokenService {
    @Value("${auth.jwt.secret}")
    private String secretKey;

    public String generateToken(String login) {

        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        Instant nowTime = Instant.now();
        Instant expiredTime = nowTime.plus(30, ChronoUnit.MINUTES);

        return JWT.create()
                .withIssuer("auth-service")
                .withAudience("todo-service")
                .withSubject(login)
                .withIssuedAt(nowTime)
                .withExpiresAt(expiredTime)
                .sign(algorithm);
    }
}
