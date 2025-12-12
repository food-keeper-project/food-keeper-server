package com.foodkeeper.foodkeeperserver.infra.clova.dto.response;

import com.foodkeeper.foodkeeperserver.infra.clova.domain.Role;

public record Message(Role role,String content) {
}
