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

    @Transactional(readOnly = true)
    public List<SelectedFoodCategory> findByFoodIds(List<Long> foodIds) {
        List<SelectedFoodCategoryEntity> entities = selectedFoodCategoryRepository.findByFoodIdIn(foodIds);
        return entities.stream()
                .map(SelectedFoodCategoryEntity::toDomain)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SelectedFoodCategory> findByFoodId(Long foodId) {
        List<SelectedFoodCategoryEntity> entities = selectedFoodCategoryRepository.findByFoodId(foodId);
        return entities.stream()
                .map(SelectedFoodCategoryEntity::toDomain)
                .toList();
    }

    @Transactional
    public void removeAllByFoodId(Long foodId) {
        selectedFoodCategoryRepository.deleteAllByFoodId(foodId);
    }

}
