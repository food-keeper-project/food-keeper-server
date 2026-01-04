package com.foodkeeper.foodkeeperserver.notification.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.notification.dataaccess.entity.FcmTokenEntity;
import com.foodkeeper.foodkeeperserver.notification.dataaccess.repository.custom.FcmTokenCustomRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@NullMarked
public interface FcmRepository extends JpaRepository<FcmTokenEntity, Long>, FcmTokenCustomRepository {

    List<FcmTokenEntity> findAllByMemberKeyIn(Set<String> memberKeys);

    Optional<FcmTokenEntity> findByToken(String token);

    void deleteByToken(String token);
}
