package com.foodkeeper.foodkeeperserver.auth.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.MemberRoleEntity;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@NullMarked
public interface MemberRoleRepository extends JpaRepository<MemberRoleEntity, Long> {
    List<MemberRoleEntity> findByMemberKey(String memberKey);
}
