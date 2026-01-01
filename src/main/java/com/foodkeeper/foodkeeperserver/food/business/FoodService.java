package com.foodkeeper.foodkeeperserver.food.business;

import com.foodkeeper.foodkeeperserver.bookmarkedfood.implement.FoodBookmarker;
import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;
import com.foodkeeper.foodkeeperserver.common.handler.TransactionHandler;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.domain.FoodCategory;
import com.foodkeeper.foodkeeperserver.food.domain.RegisteredFood;
import com.foodkeeper.foodkeeperserver.food.domain.SelectedFoodCategory;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodRegister;
import com.foodkeeper.foodkeeperserver.food.implement.*;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodReader foodReader;
    private final ImageManager imageManager;
    private final FoodManager foodManager;
    private final CategoryManager categoryManager;
    private final SelectedFoodCategoryManager selectedFoodCategoryManager;
    private final FoodBookmarker foodBookmarker;
    private final TransactionHandler transactionHandler;

    @Transactional
    public Long registerFood(FoodRegister register, MultipartFile file, String memberKey) {
        String imageUrl = imageManager.fileUpload(file).orElse("");
        transactionHandler.runOnRollback(() -> {
            if (!imageUrl.isEmpty()) {
                imageManager.deleteFile(imageUrl);
            }
        });

        Food food = foodManager.register(register, imageUrl, memberKey);
        categoryManager.findAllByIds(register.categoryIds()).forEach(category ->
                selectedFoodCategoryManager.save(SelectedFoodCategory.create(food.id(), category.id())));

        return food.id();
    }

    public SliceObject<RegisteredFood> findFoodList(Cursorable<Long> cursorable, Long categoryId, String memberKey) {
        return foodReader.findFoodList(cursorable, categoryId, memberKey);
    }

    public RegisteredFood findFood(Long foodId, String memberKey) {
        return foodReader.findFood(foodId, memberKey);
    }


    public List<RegisteredFood> findAllFoods(String memberKey) {
        return foodReader.findAll(memberKey);
    }

    public List<RegisteredFood> findImminentFoods(String memberKey) {
        LocalDate today = LocalDate.now();
        return foodReader.findImminentFoods(today, memberKey);
    }

    @Transactional
    public Long removeFood(Long foodId, String memberKey) {
        Food food = foodManager.removeFood(foodId, memberKey);
        imageManager.deleteFile(food.imageUrl());
        return food.id();
    }

    public Long bookmarkFood(Long foodId, String memberKey) {
        return foodBookmarker.bookmark(foodReader.find(foodId), memberKey);
    }

    @Transactional
    public Long updateFood(Long foodId, FoodRegister register, MultipartFile newImage, String memberKey) {
        Food food = foodReader.find(foodId);
        String imageUrl = null;
        if (newImage != null) {
            imageUrl = imageManager.fileUpload(newImage).orElse(null);
            imageManager.deleteFile(food.imageUrl());
        }
        Food updatedFood = food.update(register, imageUrl);
        foodManager.updateFood(updatedFood, register.categoryIds(), memberKey);
        return food.id();
    }
}
