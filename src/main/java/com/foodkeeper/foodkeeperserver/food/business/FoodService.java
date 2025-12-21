package com.foodkeeper.foodkeeperserver.food.business;

import com.foodkeeper.foodkeeperserver.common.domain.PageObject;
import com.foodkeeper.foodkeeperserver.food.domain.*;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodRegister;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodsFinder;
import com.foodkeeper.foodkeeperserver.food.domain.response.FoodCursorResult;
import com.foodkeeper.foodkeeperserver.food.domain.response.Foods;
import com.foodkeeper.foodkeeperserver.food.domain.response.RecipeFood;
import com.foodkeeper.foodkeeperserver.food.implement.FoodCategoryManager;
import com.foodkeeper.foodkeeperserver.food.implement.FoodManager;
import com.foodkeeper.foodkeeperserver.food.implement.ImageManager;
import com.foodkeeper.foodkeeperserver.food.implement.SelectedFoodCategoryManager;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final ImageManager imageManager;
    private final FoodManager foodManager;
    private final FoodCategoryManager foodCategoryManager;
    private final SelectedFoodCategoryManager selectedFoodCategoryManager;
    private final FoodBookmarker foodBookmarker;

    @Transactional
    public Long registerFood(FoodRegister register, MultipartFile file, String memberKey) {
        CompletableFuture<String> imageUrlFuture = imageManager.fileUpload(file);
        Food food = register.toFood(imageUrlFuture.join(), memberKey);
        try {
            Food savedFood = foodManager.register(food);
            List<FoodCategory> foodCategories = foodCategoryManager.findAllByIds(register.categoryIds());
            foodCategories.forEach(category ->
                    selectedFoodCategoryManager.save(SelectedFoodCategory.create(savedFood.id(), category.id())));
            return savedFood.id();
        } catch (RuntimeException e) { // DB 롤백 시 사진 삭제
            imageManager.deleteFile(imageUrlFuture.join());
            throw new AppException(ErrorType.DEFAULT_ERROR, e);
        }
    }

    // 커서 리스트 조회
    public FoodCursorResult getFoodList(FoodsFinder finder) {
        List<Food> foods = foodManager.findFoodList(finder);
        PageObject<Food> page = new PageObject<>(foods, finder.limit());
        Foods pagedFoods = new Foods(page.getContent());
        SelectedFoodCategories categories = new SelectedFoodCategories(selectedFoodCategoryManager.findByFoodIds(
                pagedFoods.getFoodIds()
        ));
        return new FoodCursorResult(pagedFoods.toRegisteredFoods(categories), page.hasNext());
    }

    // 단일 조회
    public RegisteredFood getFood(Long id, String memberId) {
        Food food = foodManager.findFood(id, memberId);
        List<SelectedFoodCategory> mappings = selectedFoodCategoryManager.findByFoodId(id);
        List<Long> categoryIds = mappings.stream()
                .map(SelectedFoodCategory::foodCategoryId)
                .toList();
        return food.toRegisteredFood(categoryIds);
    }


    public List<RecipeFood> getAllByMemberId(String memberId) {
        List<Food> foods = foodManager.findAllByMemberId(memberId);
        return foods.stream()
                .map(Food::toRecipe)
                .toList();
    }

    // 유통기한 임박 재료 리스트 조회
    public List<RecipeFood> getImminentFoods(String memberId) {
        List<Food> foods = foodManager.findImminentFoods(memberId);
        return foods.stream()
                .map(Food::toRecipe)
                .toList();
    }

    @Transactional
    public void removeFood(Long foodId, String memberId) {
        Food food = foodManager.findFood(foodId, memberId);
        selectedFoodCategoryManager.removeAllByFoodId(foodId);
        foodManager.removeFood(food);

        imageManager.deleteFile(food.imageUrl());
    }
    public Long bookmarkFood(Long foodId, String memberKey) {
        return foodBookmarker.bookmark(foodManager.find(foodId), memberKey);
    }
}
