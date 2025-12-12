package com.foodkeeper.foodkeeperserver.food.business;

import com.foodkeeper.foodkeeperserver.common.utils.ImageUploader;
import com.foodkeeper.foodkeeperserver.food.dto.request.FoodRegisterRequest;
import com.foodkeeper.foodkeeperserver.food.entity.Food;
import com.foodkeeper.foodkeeperserver.food.entity.FoodCategory;
import com.foodkeeper.foodkeeperserver.food.implement.FoodCategoryFinder;
import com.foodkeeper.foodkeeperserver.food.implement.FoodCreator;
import com.foodkeeper.foodkeeperserver.common.utils.S3ImageUploader;
import com.foodkeeper.foodkeeperserver.food.implement.SelectedFoodCategoryCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final ImageUploader imageUploader;
    private final FoodCreator foodCreator;
    private final FoodCategoryFinder foodCategoryFinder;
    private final SelectedFoodCategoryCreator selectedFoodCategoryCreator;

    @Transactional
    public Long registerFood(FoodRegisterRequest request, MultipartFile file, String memberId) {
        String imageUrl = imageUploader.toUrls(file);
        imageUploader.fileUpload(file,imageUrl);

        Food food = FoodRegisterRequest.toEntity(request, imageUrl, memberId);
        foodCreator.save(food);

        List<FoodCategory> foodCategories = foodCategoryFinder.findAll(request.categoryIds());

        for (FoodCategory category : foodCategories) {
            selectedFoodCategoryCreator.save(food.getId(), category.getId());
        }

        return food.getId();
    }

}
