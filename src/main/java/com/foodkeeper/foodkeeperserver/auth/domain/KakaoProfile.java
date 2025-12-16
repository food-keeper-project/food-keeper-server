package com.foodkeeper.foodkeeperserver.auth.domain;


import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoProfile(String nickname,
                           String thumbnailImageUrl,
                           String profileImageUrl,
                           Boolean isDefaultImage,
                           Boolean isDefaultNickname) {}
