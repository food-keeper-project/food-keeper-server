package com.foodkeeper.foodkeeperserver.auth.controller.v1.request;

import jakarta.validation.constraints.Size;

public record AccountDuplicationCheckRequest(@Size(max = 20) String account) {
}
