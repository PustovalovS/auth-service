package ru.pustovalov.authservice.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JwtTokenService {

    public String generateToken(String login) {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        String secretKey = new BigInteger(1, bytes).toString(16);

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
