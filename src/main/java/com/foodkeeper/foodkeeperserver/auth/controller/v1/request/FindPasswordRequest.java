package com.foodkeeper.foodkeeperserver.auth.controller.v1.request;

import com.foodkeeper.foodkeeperserver.auth.controller.v1.validation.ValidAccount;
import jakarta.validation.constraints.Email;

public record FindPasswordRequest(@Email String email, @ValidAccount String account) {
}
