package com.foodkeeper.foodkeeperserver.member.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.MemberEntity;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@NullMarked
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByMemberKey(String memberKey);
}
