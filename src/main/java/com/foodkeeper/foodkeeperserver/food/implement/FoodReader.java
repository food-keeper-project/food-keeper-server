package com.foodkeeper.foodkeeperserver.food.implement;

import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodRepository;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.domain.FoodCategories;
import com.foodkeeper.foodkeeperserver.food.domain.RegisteredFood;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FoodReader {

    private final FoodRepository foodRepository;
    private final FoodCategoryReader foodCategoryReader;

    public SliceObject<RegisteredFood> findFoodList(Cursorable<Long> cursorable, Long categoryId, String memberKey) {
        SliceObject<Food> foods = foodRepository.findFoodCursorList(cursorable, categoryId, memberKey).map(FoodEntity::toDomain);
        FoodCategories foodCategories = foodCategoryReader.findNamesByFoodIds(foods.content().stream().map(Food::id).toList());
        return foods.map(food -> food.toRegisteredFood(foodCategories.getCategories(food.id())));
    }

    public RegisteredFood findFood(Long id, String memberKey) {
        Food food = foodRepository.findByIdAndMemberKey(id, memberKey).orElseThrow(() -> new AppException(ErrorType.FOOD_DATA_NOT_FOUND)).toDomain();
        FoodCategories foodCategories = foodCategoryReader.findNamesFoodById(food.id());
        return food.toRegisteredFood(foodCategories.getCategories(food.id()));
    }

    public List<RegisteredFood> findAll(String memberKey) {
        List<Food> foods = foodRepository.findAllByMemberKey(memberKey).stream()
                .map(FoodEntity::toDomain).toList();
        FoodCategories foodCategories = foodCategoryReader.findNamesByFoodIds(foods.stream().map(Food::id).toList());
        return foods.stream().map(food -> food.toRegisteredFood(foodCategories.getCategories(food.id()))).toList();
    }

    public List<RegisteredFood> findImminentFoods(LocalDate toady, String memberKey) {
        List<Food> foods = foodRepository.findImminentFoods(memberKey).stream()
                .filter(food -> food.isImminent(toady))
                .map(FoodEntity::toDomain).toList();
        FoodCategories foodCategories = foodCategoryReader.findNamesByFoodIds(foods.stream().map(Food::id).toList());
        return foods.stream().map(food -> food.toRegisteredFood(foodCategories.getCategories(food.id()))).toList();
    }

    public Food find(Long foodId) {
        return foodRepository.findById(foodId).orElseThrow(() -> new AppException(ErrorType.NOT_FOUND_DATA)).toDomain();
    }

    public List<Food> findFoodsToNotify(LocalDate today) {
        return foodRepository.findAll().stream()
                .filter(food -> food.isNotificationDay(today))
                .map(FoodEntity::toDomain)
                .toList();
    }
}
