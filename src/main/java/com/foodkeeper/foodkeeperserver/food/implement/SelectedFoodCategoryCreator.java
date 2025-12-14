package com.foodkeeper.foodkeeperserver.food.implement;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.SelectedFoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.SelectedFoodCategoryRepository;
import com.foodkeeper.foodkeeperserver.food.domain.SelectedFoodCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SelectedFoodCategoryCreator {

    private final SelectedFoodCategoryRepository selectedFoodCategoryRepository;

    public Long save(SelectedFoodCategory selectedFoodCategory) {
        SelectedFoodCategoryEntity selectedFoodCategoryEntity = SelectedFoodCategoryEntity.from(selectedFoodCategory);
        return selectedFoodCategoryRepository.save(selectedFoodCategoryEntity).getId();
    }
}
