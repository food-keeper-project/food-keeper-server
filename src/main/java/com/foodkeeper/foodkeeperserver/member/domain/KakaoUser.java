package com.foodkeeper.foodkeeperserver.member.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoUser(Long id, LocalDateTime connectedAt, KakaoAccount kakaoAccount) {}
