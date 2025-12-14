package com.foodkeeper.foodkeeperserver.clova.controller.response;

import com.foodkeeper.foodkeeperserver.clova.domain.Role;

public record Message(Role role, String content) {
}
