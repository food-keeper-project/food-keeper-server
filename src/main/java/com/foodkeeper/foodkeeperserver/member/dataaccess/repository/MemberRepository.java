package com.foodkeeper.foodkeeperserver.member.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.MemberEntity;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@NullMarked
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByMemberKey(String memberKey);

    @Query("SELECT m.refreshToken FROM MemberEntity m WHERE m.memberKey = :memberKey")
    Optional<String> findRefreshToken(@Param("memberKey") String memberKey);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE MemberEntity m SET m.refreshToken = :refreshToken WHERE m.memberKey = :memberKey")
    void updateRefreshToken(@Param("memberKey") String memberKey, @Param("refreshToken") String refreshToken);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE MemberEntity m SET m.refreshToken = null WHERE m.memberKey = :memberKey")
    void deleteRefreshTokenByMemberKey(@Param("memberKey") String memberKey);

    boolean existsByEmail(String email);
}
