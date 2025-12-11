package com.foodkeeper.foodkeeperserver.member.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, String> {
}
