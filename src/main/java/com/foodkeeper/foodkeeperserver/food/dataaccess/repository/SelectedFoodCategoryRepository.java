package com.foodkeeper.foodkeeperserver.food.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.SelectedFoodCategoryEntity;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

@NullMarked
public interface SelectedFoodCategoryRepository extends JpaRepository<SelectedFoodCategoryEntity, Long> {
    List<SelectedFoodCategoryEntity> findByFoodIdIn(List<Long> foodIds);

    List<SelectedFoodCategoryEntity> findByFoodId(Long foodId);

    void deleteAllByFoodId(Long foodId);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM SelectedFoodCategoryEntity sf WHERE sf.foodId in :foodIds")
    void deleteAllByFoodIds(@Param("foodIds") Collection<Long> foodIds);
}
