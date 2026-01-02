package com.foodkeeper.foodkeeperserver.auth.controller.v1.request;

import com.foodkeeper.foodkeeperserver.member.domain.enums.Gender;

import java.util.List;

public record LocalSignUpRequest(String account,
                                 String password,
                                 String email,
                                 String nickname,
                                 Gender gender,
                                 List<String> preferFoods) {
}
