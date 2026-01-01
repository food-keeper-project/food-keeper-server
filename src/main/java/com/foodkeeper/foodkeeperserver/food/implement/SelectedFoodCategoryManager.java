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

    // 다 지우고 다시 생성
    @Transactional
    public void update(Long foodId, List<Long> categoryIds) {
        removeAllByFoodId(foodId);

        if (categoryIds != null && !categoryIds.isEmpty()) {
            List<SelectedFoodCategoryEntity> newCategory = categoryIds.stream()
                    .map(categoryId -> SelectedFoodCategory.create(foodId, categoryId))
                    .map(SelectedFoodCategoryEntity::from)
                    .toList();
            selectedFoodCategoryRepository.saveAll(newCategory);
        }
    }

}
