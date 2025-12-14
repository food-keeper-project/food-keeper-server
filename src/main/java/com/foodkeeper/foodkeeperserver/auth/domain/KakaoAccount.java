package com.foodkeeper.foodkeeperserver.auth.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoAccount(Boolean profileNeedsAgreement,
                           Boolean profileNicknameNeedsAgreement,
                           Boolean profileImageNeedsAgreement,
                           String email,
                           KakaoProfile profile) {}
