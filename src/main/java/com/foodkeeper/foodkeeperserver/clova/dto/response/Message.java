package com.foodkeeper.foodkeeperserver.clova.dto.response;

import com.foodkeeper.foodkeeperserver.clova.domain.Role;

public record Message(Role role,String content) {
}
