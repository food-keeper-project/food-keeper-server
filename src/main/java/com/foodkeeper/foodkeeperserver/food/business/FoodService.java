package com.foodkeeper.foodkeeperserver.food.business;

import com.foodkeeper.foodkeeperserver.food.implement.ImageManager;
import com.foodkeeper.foodkeeperserver.food.dto.request.FoodRegisterRequest;
import com.foodkeeper.foodkeeperserver.food.entity.Food;
import com.foodkeeper.foodkeeperserver.food.entity.FoodCategory;
import com.foodkeeper.foodkeeperserver.food.implement.FoodCategoryFinder;
import com.foodkeeper.foodkeeperserver.food.implement.FoodCreator;
import com.foodkeeper.foodkeeperserver.food.implement.SelectedFoodCategoryCreator;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final ImageManager imageManager;
    private final FoodCreator foodCreator;
    private final FoodCategoryFinder foodCategoryFinder;
    private final SelectedFoodCategoryCreator selectedFoodCategoryCreator;

    @Transactional
    public Long registerFood(FoodRegisterRequest request, MultipartFile file, String memberId) {


        String imageUrl = imageManager.fileUpload(file);
        try {
            Food food = FoodRegisterRequest.toEntity(request, imageUrl, memberId);
            Food savedFood = foodCreator.save(food);

            //todo 카테고리 선택 방식에 따라 인자값 수정
            List<FoodCategory> foodCategories = foodCategoryFinder.findAll(request.categoryIds());
            foodCategories.forEach(category -> selectedFoodCategoryCreator.save(savedFood.getId(), category.getId()));
            return savedFood.getId();
        } catch (RuntimeException e) {
            imageManager.deleteFile(imageUrl);
            throw new AppException(ErrorType.DEFAULT_ERROR, e);
        }
    }

}
