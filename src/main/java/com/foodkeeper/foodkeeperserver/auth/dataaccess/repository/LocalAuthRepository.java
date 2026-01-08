package com.foodkeeper.foodkeeperserver.auth.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.LocalAuthEntity;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.custom.LocalAuthCustomRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@NullMarked
public interface LocalAuthRepository extends JpaRepository<LocalAuthEntity, Long>, LocalAuthCustomRepository {

    @Query(value = "SELECT GET_LOCK(:key, :timeout)", nativeQuery = true)
    Integer getLock(@Param("key") String key, @Param("timeout") int timeout);

    @Query(value = "SELECT RELEASE_LOCK(:key)", nativeQuery = true)
    Integer releaseLock(@Param("key") String key);
}
