package com.foodkeeper.foodkeeperserver.food.business;

import com.foodkeeper.foodkeeperserver.food.domain.*;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodsFinder;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodRegister;
import com.foodkeeper.foodkeeperserver.food.domain.response.FoodCursorResult;
import com.foodkeeper.foodkeeperserver.food.domain.response.FoodSlice;
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

    @Transactional
    public Long registerFood(FoodRegister register, MultipartFile file, String memberId) {
        CompletableFuture<String> imageUrlFuture = imageManager.fileUpload(file);
        Food food = register.toFood(imageUrlFuture.join(), memberId);
        try {
            Food savedFood = foodManager.register(food);
            //todo 카테고리 선택 방식에 따라 인자값 수정, 카테고리 선택 시에 매번 모두 조회?
            List<FoodCategory> foodCategories = foodCategoryManager.findAll(register.categoryIds());
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
        FoodSlice foodSlice = FoodSlice.from(foods, finder.limit());
        SelectedFoodCategories categories = new SelectedFoodCategories(selectedFoodCategoryManager.findByFoodIds(
                foodSlice.foods().getFoodIds()
        ));
        return foodSlice.toResult(categories);
    }

    // 단일 조회
    public RegisteredFood getFood(Long id, String memberId) {
        Food food = foodManager.findFood(id, memberId);
        List<SelectedFoodCategory> mappings = selectedFoodCategoryManager.findByFoodId(id);
        List<Long> categoryIds = mappings.stream()
                .map(SelectedFoodCategory::foodCategoryId)
                .toList();
        return food.toFood(categoryIds);
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

}
