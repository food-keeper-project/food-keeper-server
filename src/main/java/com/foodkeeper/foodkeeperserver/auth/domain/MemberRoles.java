package com.foodkeeper.foodkeeperserver.auth.domain;

import com.foodkeeper.foodkeeperserver.auth.domain.enums.MemberRole;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.function.Consumer;

@NullMarked
public record MemberRoles(List<MemberRole> roles) {

    public void forEach(Consumer<? super MemberRole> action) {
        for (MemberRole role : roles) {
            action.accept(role);
        }
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        return roles.stream().map(MemberRole::name).map(SimpleGrantedAuthority::new).toList();
    }
}
