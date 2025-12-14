package com.foodkeeper.foodkeeperserver.food.business;

import com.foodkeeper.foodkeeperserver.food.business.request.FoodRegisterDto;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.domain.FoodCategory;
import com.foodkeeper.foodkeeperserver.food.domain.SelectedFoodCategory;
import com.foodkeeper.foodkeeperserver.food.implement.ImageManager;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.food.implement.FoodCategoryFinder;
import com.foodkeeper.foodkeeperserver.food.implement.FoodRegister;
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
    private final FoodRegister foodRegister;
    private final FoodCategoryFinder foodCategoryFinder;
    private final SelectedFoodCategoryCreator selectedFoodCategoryCreator;

    @Transactional
    public Long registerFood(FoodRegisterDto dto, MultipartFile file, String memberId) {
        String imageUrl = imageManager.fileUpload(file);
        Food food = dto.toDomain(imageUrl,memberId);
        try {
            Food saveFood = foodRegister.register(food);
            //todo 카테고리 선택 방식에 따라 인자값 수정
            List<FoodCategory> foodCategories = foodCategoryFinder.findAll(dto.categoryIds());
            foodCategories.forEach(category ->
                    selectedFoodCategoryCreator.save(SelectedFoodCategory.create(food.id(), category.id())));
            return saveFood.id();
        } catch (RuntimeException e) { // DB 롤백 시 사진 삭제
            imageManager.deleteFile(imageUrl);
            throw new AppException(ErrorType.DEFAULT_ERROR, e);
        }
    }

}
