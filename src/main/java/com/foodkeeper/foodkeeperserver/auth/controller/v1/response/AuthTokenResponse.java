package com.foodkeeper.foodkeeperserver.auth.controller.v1.response;

public record AuthTokenResponse(String accessToken, String refreshToken) {
}