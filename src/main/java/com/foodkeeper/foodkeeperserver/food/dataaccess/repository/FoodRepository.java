package com.foodkeeper.foodkeeperserver.food.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.custom.FoodRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface FoodRepository extends JpaRepository<FoodEntity,Long>, FoodRepositoryCustom {
    Optional<FoodEntity> findByIdAndMemberId(Long id, String memberId);

    @Query("select f.name from FoodEntity f where f.id in :ids and f.memberId = :memberId")
    List<String> findNamesByIdsAndMemberId(@Param("ids") List<Long> ids, @Param("memberId") String memberId);
}
