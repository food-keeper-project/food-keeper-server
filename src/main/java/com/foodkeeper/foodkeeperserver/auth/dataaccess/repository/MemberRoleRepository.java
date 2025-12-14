package com.foodkeeper.foodkeeperserver.auth.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.MemberRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRoleRepository extends JpaRepository<MemberRoleEntity, Long> {
    List<MemberRoleEntity> findByMemberKey(String memberKey);
}
