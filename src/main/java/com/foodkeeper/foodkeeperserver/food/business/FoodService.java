package com.foodkeeper.foodkeeperserver.food.business;

import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.domain.FoodCategory;
import com.foodkeeper.foodkeeperserver.food.domain.MyFood;
import com.foodkeeper.foodkeeperserver.food.domain.SelectedFoodCategory;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodCursorFinder;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodRegister;
import com.foodkeeper.foodkeeperserver.food.domain.response.FoodCursorResult;
import com.foodkeeper.foodkeeperserver.food.domain.response.ImminentFood;
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
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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

        boolean hasNext = false;
        if (foods.size() > finder.limit()) {
            hasNext = true;
            foods.remove(finder.limit().intValue()); // 가져온 확인용 다음 페이지 제거
        }
        // 식재료 카테고리Id 값들 조회
        List<Long> foodIds = foods.stream().map(Food::id).toList();
        List<SelectedFoodCategory> mappings = selectedFoodCategoryManager.findByFoodIds(foodIds);

        // key : foodId, value : List<CategoryId>
        Map<Long, List<Long>> categoryMap = mappings.stream()
                .collect(Collectors.groupingBy(
                        SelectedFoodCategory::foodId,
                        Collectors.mapping(SelectedFoodCategory::foodCategoryId, Collectors.toList())
                ));

        // 응답 객체 변환
        List<MyFood> foodResponses = foods.stream()
                .map(food -> {
                    List<Long> categoryIds = categoryMap.getOrDefault(food.id(), List.of());
                    return MyFood.of(food, categoryIds);
                })
                .toList();
        return new FoodCursorResult(foodResponses, hasNext);
    }

    // 단일 조회
    @Transactional(readOnly = true)
    public MyFood getFood(Long id, String memberId) {
        Food food = foodManager.findFood(id, memberId);
        List<SelectedFoodCategory> mappings = selectedFoodCategoryManager.findByFoodId(id);
        List<Long> categoryIds = mappings.stream()
                .map(SelectedFoodCategory::foodCategoryId)
                .toList();
        return MyFood.of(food, categoryIds);
    }

    // 이름 조회 - ai 용
    @Transactional(readOnly = true)
    public List<String> getFoodNames(List<Long> ids, String memberId) {
        return foodManager.findFoodNames(ids, memberId);
    }

    // 유통기한 임박 재료 리스트 조회
    @Transactional(readOnly = true)
    public List<ImminentFood> getImminentFoods(String memberId) {
        List<Food> foods = foodManager.findImminentFoods(memberId);
        return foods.stream()
                .map(ImminentFood::of)
                .toList();
    }

}
