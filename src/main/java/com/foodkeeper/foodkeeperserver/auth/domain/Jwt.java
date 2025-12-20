package com.foodkeeper.foodkeeperserver.auth.domain;

public record Jwt(String accessToken, String refreshToken) {
}
