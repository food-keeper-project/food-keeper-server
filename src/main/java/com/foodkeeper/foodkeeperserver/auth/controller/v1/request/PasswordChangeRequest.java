package com.foodkeeper.foodkeeperserver.auth.controller.v1.request;

import com.foodkeeper.foodkeeperserver.auth.controller.v1.validation.ValidAccount;
import com.foodkeeper.foodkeeperserver.auth.controller.v1.validation.ValidPassword;
import jakarta.validation.constraints.Email;

public record PasswordChangeRequest(@Email String email,
                                    @ValidAccount String account,
                                    @ValidPassword String newPassword) {
}
