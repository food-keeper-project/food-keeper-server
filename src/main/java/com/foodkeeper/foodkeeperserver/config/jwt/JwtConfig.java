package com.foodkeeper.foodkeeperserver.config.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Key;
import java.util.Base64;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Bean
    public Key key() {
        return Keys.hmacShaKeyFor(Base64.getEncoder().encode(secretKey.getBytes()));
    }
}
