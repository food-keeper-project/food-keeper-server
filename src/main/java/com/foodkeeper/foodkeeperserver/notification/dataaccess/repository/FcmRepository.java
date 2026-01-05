package com.foodkeeper.foodkeeperserver.notification.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.notification.dataaccess.entity.FcmTokenEntity;
import com.foodkeeper.foodkeeperserver.notification.dataaccess.repository.custom.FcmTokenCustomRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

@NullMarked
public interface FcmRepository extends JpaRepository<FcmTokenEntity, Long>, FcmTokenCustomRepository {
}
