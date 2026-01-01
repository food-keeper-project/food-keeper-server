package com.foodkeeper.foodkeeperserver.food.implement;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.SelectedFoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.SelectedFoodCategoryRepository;
import com.foodkeeper.foodkeeperserver.food.domain.SelectedFoodCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SelectedFoodCategoryManager {

    private final SelectedFoodCategoryRepository selectedFoodCategoryRepository;

    public void save(SelectedFoodCategory selectedFoodCategory) {
        SelectedFoodCategoryEntity selectedFoodCategoryEntity = SelectedFoodCategoryEntity.from(selectedFoodCategory);
        selectedFoodCategoryRepository.save(selectedFoodCategoryEntity);
    }


    @Transactional
    public void removeAllByFoodId(Long foodId) {
        selectedFoodCategoryRepository.deleteAllByFoodId(foodId);
    }

    @Transactional
    public void removeAllByFoodIds(List<Long> foodIds) {
        selectedFoodCategoryRepository.deleteAllByFoodIds(foodIds);
    }
}
