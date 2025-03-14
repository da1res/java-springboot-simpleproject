package com.academiajava.backend.security;

import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.academiajava.backend.domain.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

@Service
public class JwtService {
    @Value("${api.security.token.secret}")
    private String SECRET_KEY;

    public String generateToken(User user){
        try{
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            return JWT.create().withIssuer("academia-java").withSubject(user.getUsername()).withExpiresAt(generateExpirationDate()).sign(algorithm);
        }catch(JWTCreationException exception){
            throw new RuntimeException("Error while generating token!", exception);
        }catch (IllegalArgumentException exception){
            throw new RuntimeException("Argument error!", exception);
        }
    }

    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            return JWT.require(algorithm).withIssuer("academia-java").build().verify(token).getSubject();
        } catch (JWTVerificationException e) {
            return ""; // Não retorna user nenhum, unauthorized 
        }

    }

    private Date generateExpirationDate() {
        return Date.from(Instant.now().plusSeconds(2 * 60 * 60));
    }

}
