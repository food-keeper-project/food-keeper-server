package com.foodkeeper.foodkeeperserver.food.business;

import com.foodkeeper.foodkeeperserver.bookmarkedfood.implement.FoodBookmarker;
import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;
import com.foodkeeper.foodkeeperserver.food.domain.*;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodRegister;
import com.foodkeeper.foodkeeperserver.food.implement.FoodCategoryManager;
import com.foodkeeper.foodkeeperserver.food.implement.FoodManager;
import com.foodkeeper.foodkeeperserver.food.implement.ImageManager;
import com.foodkeeper.foodkeeperserver.food.implement.SelectedFoodCategoryManager;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.food.domain.RecipeFood;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
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
        Food food = register.toNewFood(imageUrlFuture.join(), memberKey);
        try {
            Food savedFood = foodManager.registerFood(food);
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
    public SliceObject<RegisteredFood> getFoodList(Cursorable<LocalDateTime> cursorable, Long categoryId, String memberKey) {
        SliceObject<Food> foods = foodManager.findFoodList(cursorable, categoryId, memberKey);
        SelectedFoodCategories categories = new SelectedFoodCategories(selectedFoodCategoryManager.findByFoodIds(
                foods.content().stream().map(Food::id).toList()
        ));
        return foods.map(food -> food.toRegisteredFood(categories.getCategoryIdsByFoodId(food.id())));
    }

    // 단일 조회
    public RegisteredFood getFood(Long id, String memberKey) {
        Food food = foodManager.findFood(id, memberKey);
        List<SelectedFoodCategory> mappings = selectedFoodCategoryManager.findByFoodId(id);
        List<Long> categoryIds = mappings.stream()
                .map(SelectedFoodCategory::foodCategoryId)
                .toList();
        return food.toRegisteredFood(categoryIds);
    }


    public List<RecipeFood> getAllByMemberKey(String memberKey) {
        List<Food> foods = foodManager.findAllByMemberKey(memberKey);
        return foods.stream()
                .map(Food::toRecipe)
                .toList();
    }

    // 유통기한 임박 재료 리스트 조회
    public List<RecipeFood> getImminentFoods(String memberKey) {
        List<Food> foods = foodManager.findImminentFoods(memberKey);
        return foods.stream()
                .map(Food::toRecipe)
                .toList();
    }

    @Transactional
    public void removeFood(Long foodId, String memberKey) {
        Food food = foodManager.findFood(foodId, memberKey);
        selectedFoodCategoryManager.removeAllByFoodId(foodId);
        foodManager.removeFood(food);

        imageManager.deleteFile(food.imageUrl());
    }

    public Long bookmarkFood(Long foodId, String memberKey) {
        return foodBookmarker.bookmark(foodManager.find(foodId), memberKey);
    }

    @Transactional
    public Long updateFood(Long foodId, FoodRegister foodRegister, MultipartFile imageUrl, String memberKey) {

        CompletableFuture<String> imageUrlFuture = imageManager.fileUpload(imageUrl);
        Food food = foodRegister.toNewFood(imageUrlFuture.join(), memberKey);

        foodManager.updateFood(food,foodRegister.categoryIds(), imageUrl);
        imageManager.fileUpload(imageUrl);
        return food.id();
    }
}
