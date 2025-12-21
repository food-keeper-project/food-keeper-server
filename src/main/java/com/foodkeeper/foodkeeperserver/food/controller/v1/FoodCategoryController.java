package com.foodkeeper.foodkeeperserver.food.controller.v1;


import com.foodkeeper.foodkeeperserver.food.business.FoodCategoryService;
import com.foodkeeper.foodkeeperserver.food.controller.v1.request.FoodCategoryRegisterRequest;
import com.foodkeeper.foodkeeperserver.food.controller.v1.response.FoodCategoryResponse;
import com.foodkeeper.foodkeeperserver.food.domain.FoodCategory;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodCategoryRegister;
import com.foodkeeper.foodkeeperserver.support.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "FoodCategory", description = "카테고리 관련 API")
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class FoodCategoryController {

    private final FoodCategoryService foodCategoryService;

    @NullMarked
    @Operation(summary = "카테고리 추가", description = "카테고리 추가 API")
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createCategory(@RequestBody FoodCategoryRegisterRequest request) {
        String memberKey = "memberKey"; // todo 로그인 방식 구현 후 리팩토링
        FoodCategoryRegister register = new FoodCategoryRegister(request.name(), memberKey);
        foodCategoryService.registerFoodCategory(register);
        return ResponseEntity.created(URI.create("/api/v1/categories")).build();
    }

    @NullMarked
    @Operation(summary = "카테고리 전체 조회", description = "카테고리 전체 조회 API")
    @GetMapping
    public ResponseEntity<ApiResponse<List<FoodCategoryResponse>>> getCategories() {
        String memberKey = "memberKey"; // todo 로그인 방식 구현 후 리팩토링
        List<FoodCategory> foodCategories = foodCategoryService.findAllByMemberKey(memberKey);
        List<FoodCategoryResponse> responses = foodCategories.stream()
                .map(FoodCategoryResponse::from)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }
}
