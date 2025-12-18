package com.foodkeeper.foodkeeperserver.food.business;

import com.foodkeeper.foodkeeperserver.food.domain.*;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodCursorFinder;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodRegister;
import com.foodkeeper.foodkeeperserver.food.domain.response.FoodCursorResult;
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
    @Transactional(readOnly = true)
    public FoodCursorResult getFoodList(FoodCursorFinder finder) {
        List<Food> foods = foodManager.findFoodList(finder);
        FoodSlice foodSlice = FoodSlice.from(foods, finder.limit());
        SelectedFoodCategories categories = new SelectedFoodCategories(selectedFoodCategoryManager.findByFoodIds(
                foodSlice.foods().getFoodIds()
        ));
        return foodSlice.toResult(categories);
    }

    // 단일 조회
    @Transactional(readOnly = true)
    public RegisteredFood getFood(Long id, String memberId) {
        Food food = foodManager.findFood(id, memberId);
        List<SelectedFoodCategory> mappings = selectedFoodCategoryManager.findByFoodId(id);
        List<Long> categoryIds = mappings.stream()
                .map(SelectedFoodCategory::foodCategoryId)
                .toList();
        return RegisteredFood.of(food, categoryIds);
    }


    @Transactional(readOnly = true)
    public List<RecipeFood> getAllByMemberId(String memberId) {
        List<Food> foods = foodManager.findAllByMemberId(memberId);
        return foods.stream()
                .map(RecipeFood::of)
                .toList();
    }

    // 유통기한 임박 재료 리스트 조회
    @Transactional(readOnly = true)
    public List<RecipeFood> getImminentFoods(String memberId) {
        List<Food> foods = foodManager.findImminentFoods(memberId);
        return foods.stream()
                .map(RecipeFood::of)
                .toList();
    }

}
/**
 * 원래는 RecipeFood, ImminentFood 도메인 객체를 분리해야될지 고민하다가
 * 두 객체의 성질이 유통기한 임박 시간을 계산해주는지 여부에 따라 값이 달라지는 것 말고는 똑같은 역할을 하는 객체라고 생각이 들어서 하나로 통일했습니다
 * 모든 식재료를 조회했을 때의 도메인 객체와, 유통기한 임박 설정 기간에 따라 값이 필터링 되는 도메인 객체를 나눠야 하는지 원호님 생각이 궁금합니다.
 */