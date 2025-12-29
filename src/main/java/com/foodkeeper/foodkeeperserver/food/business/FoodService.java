package com.foodkeeper.foodkeeperserver.food.business;

import com.foodkeeper.foodkeeperserver.bookmarkedfood.implement.FoodBookmarker;
import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;
import com.foodkeeper.foodkeeperserver.food.domain.*;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodRegister;
import com.foodkeeper.foodkeeperserver.food.implement.*;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodReader foodReader;
    private final ImageManager imageManager;
    private final FoodManager foodManager;
    private final FoodCategoryManager foodCategoryManager;
    private final SelectedFoodCategoryManager selectedFoodCategoryManager;
    private final FoodBookmarker foodBookmarker;
    private final FoodProvider foodProvider;

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

    public SliceObject<RegisteredFood> getFoodList(Cursorable<LocalDateTime> cursorable, Long categoryId, String memberKey) {
        SliceObject<Food> foods = foodReader.findFoodList(cursorable, categoryId, memberKey);
        return foodProvider.getFoodList(foods);
    }

    // 단일 조회
    public RegisteredFood getFood(Long foodId, String memberKey) {
        Food food = foodReader.findFood(foodId, memberKey);
        return foodProvider.getFood(food);
    }


    public List<RegisteredFood> getAllFoods(String memberKey) {
        List<Food> foods = foodReader.findAll(memberKey);
        return foodProvider.getAllFoods(foods);
    }

    // 유통기한 임박 재료 리스트 조회
    public List<RegisteredFood> getImminentFoods(String memberKey) {
        List<Food> foods = foodReader.findImminentFoods(memberKey);
        return foodProvider.getAllFoods(foods);
    }

    @Transactional
    public void removeFood(Long foodId, String memberKey) {
        Food food = foodReader.findFood(foodId, memberKey);
        selectedFoodCategoryManager.removeAllByFoodId(foodId);
        foodManager.removeFood(food);

        imageManager.deleteFile(food.imageUrl());
    }

    public Long bookmarkFood(Long foodId, String memberKey) {
        return foodBookmarker.bookmark(foodReader.find(foodId), memberKey);
    }
}
