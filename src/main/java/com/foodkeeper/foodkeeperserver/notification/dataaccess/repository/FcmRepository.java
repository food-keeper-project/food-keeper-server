package com.foodkeeper.foodkeeperserver.notification.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.notification.dataaccess.entity.FcmTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FcmRepository extends JpaRepository<FcmTokenEntity, Long> {

    List<FcmTokenEntity> findAllByMemberKeyIn(List<String> memberKeys);

    Optional<FcmTokenEntity> findByToken(String token);

    void deleteByToken(String token);
}
