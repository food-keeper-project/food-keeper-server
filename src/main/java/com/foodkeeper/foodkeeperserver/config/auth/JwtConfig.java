package com.foodkeeper.foodkeeperserver.config.auth;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.util.Base64;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Bean
    public SecretKey key() {
        return Keys.hmacShaKeyFor(Base64.getEncoder().encode(secretKey.getBytes()));
    }
}
