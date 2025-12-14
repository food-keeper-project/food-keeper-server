package com.foodkeeper.foodkeeperserver.member.controller.v1.response;

public record AuthTokenResponse(String accessToken, String refreshToken) {
}