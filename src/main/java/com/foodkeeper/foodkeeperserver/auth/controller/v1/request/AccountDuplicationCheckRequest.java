package com.foodkeeper.foodkeeperserver.auth.controller.v1.request;

import com.foodkeeper.foodkeeperserver.auth.controller.v1.validation.ValidAccount;

public record AccountDuplicationCheckRequest(@ValidAccount String account) {
}
