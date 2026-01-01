package com.foodkeeper.foodkeeperserver.notification.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.notification.dataaccess.entity.FcmTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FcmRepository extends JpaRepository<FcmTokenEntity, Long> {

    List<FcmTokenEntity> findAllByMemberKeyIn(Set<String> memberKeys);

    Optional<FcmTokenEntity> findByToken(String token);

    void deleteByToken(String token);
}
