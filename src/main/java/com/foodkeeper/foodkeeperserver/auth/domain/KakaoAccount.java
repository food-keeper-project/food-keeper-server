package com.foodkeeper.foodkeeperserver.auth.domain;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoAccount(Boolean profileNeedsAgreement,
                           Boolean profileNicknameNeedsAgreement,
                           Boolean profileImageNeedsAgreement,
                           String email,
                           KakaoProfile profile) {}
