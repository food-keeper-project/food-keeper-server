package com.foodkeeper.foodkeeperserver.member.domain;

import lombok.Builder;

@Builder
public record NewLocalMember(NewMember member,
                             String account,
                             String password) {
}
