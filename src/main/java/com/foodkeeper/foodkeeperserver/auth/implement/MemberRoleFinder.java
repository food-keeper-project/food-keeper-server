package com.foodkeeper.foodkeeperserver.auth.implement;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.MemberRoleEntity;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.MemberRoleRepository;
import com.foodkeeper.foodkeeperserver.auth.domain.MemberRoles;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberRoleFinder {

    private final MemberRoleRepository memberRoleRepository;

    public MemberRoles findAll(String memberKey) {
        return new MemberRoles(memberRoleRepository.findByMemberKey(memberKey).stream()
                .map(MemberRoleEntity::getRole)
                .toList());
    }
}
