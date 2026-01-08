package com.foodkeeper.foodkeeperserver.auth.controller.v1.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailCodeVerifyRequest(@Email String email,
                                     @NotBlank String code) {
}
