package com.foodkeeper.foodkeeperserver.auth.domain;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public record MemberRoles(List<MemberRole> roles) {

    public List<SimpleGrantedAuthority> getAuthorities() {
        return roles.stream().map(MemberRole::name).map(SimpleGrantedAuthority::new).toList();
    }
}
