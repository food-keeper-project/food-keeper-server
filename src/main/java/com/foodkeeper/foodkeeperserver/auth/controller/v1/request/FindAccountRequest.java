package com.foodkeeper.foodkeeperserver.auth.controller.v1.request;

import jakarta.validation.constraints.Email;

public record FindAccountRequest(@Email String email) {
}
