package com.foodkeeper.foodkeeperserver.food.business;

import com.foodkeeper.foodkeeperserver.food.controller.v1.response.FoodListResponse;
import com.foodkeeper.foodkeeperserver.food.controller.v1.response.MyFoodResponse;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.SelectedFoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodCursorFinder;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodRegister;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.domain.FoodCategory;
import com.foodkeeper.foodkeeperserver.food.domain.SelectedFoodCategory;
import com.foodkeeper.foodkeeperserver.food.implement.ImageManager;
import com.foodkeeper.foodkeeperserver.food.implement.FoodCategoryManager;
import com.foodkeeper.foodkeeperserver.food.implement.FoodManager;
import com.foodkeeper.foodkeeperserver.food.implement.SelectedFoodCategoryManager;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final ImageManager imageManager;
    private final FoodManager foodManager;
    private final FoodCategoryManager foodCategoryManager;
    private final SelectedFoodCategoryManager selectedFoodCategoryManager;

    @Transactional
    public Long registerFood(FoodRegister dto, MultipartFile file, String memberId) {
        String imageUrl = imageManager.fileUpload(file); // 비동기 업로드
        Food food = dto.toDomain(imageUrl, memberId);
        try {
            Food savedFood = foodManager.register(food);
            //todo 카테고리 선택 방식에 따라 인자값 수정, 카테고리 선택 시에 매번 모두 조회?
            List<FoodCategory> foodCategories = foodCategoryManager.findAll(dto.categoryIds());
            foodCategories.forEach(category ->
                    selectedFoodCategoryManager.save(SelectedFoodCategory.create(savedFood.id(), category.id())));
            return savedFood.id();
        } catch (RuntimeException e) { // DB 롤백 시 사진 삭제
            imageManager.deleteFile(imageUrl);
            throw new AppException(ErrorType.DEFAULT_ERROR, e);
        }
    }

    // 커서 리스트 조회
    @Transactional(readOnly = true)
    public FoodListResponse getFoodList(FoodCursorFinder finder) {
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
        List<MyFoodResponse> foodResponses = foods.stream()
                .map(food -> {
                    List<Long> categoryIds = categoryMap.getOrDefault(food.id(), List.of());
                    return MyFoodResponse.toFoodResponse(food, categoryIds);
                })
                .toList();
        return new FoodListResponse(foodResponses, hasNext);
    }

    // 단일 조회
    @Transactional(readOnly = true)
    public MyFoodResponse getFood(Long id, String memberId) {
        Food food = foodManager.findFood(id, memberId);
        List<SelectedFoodCategory> mappings = selectedFoodCategoryManager.findByFoodId(id);
        List<Long> categoryIds = mappings.stream()
                .map(SelectedFoodCategory::foodCategoryId)
                .toList();
        return MyFoodResponse.toFoodResponse(food,categoryIds);
    }
}
