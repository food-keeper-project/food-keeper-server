package com.foodkeeper.foodkeeperserver.auth.controller.v1.request;

import com.foodkeeper.foodkeeperserver.auth.controller.v1.validation.ValidAccount;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailCodeVerifyRequest(@Email String email,
                                     @ValidAccount String account,
                                     @NotBlank String code) {
}
