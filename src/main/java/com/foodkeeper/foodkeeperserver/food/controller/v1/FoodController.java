package com.foodkeeper.foodkeeperserver.food.controller.v1;

import com.foodkeeper.foodkeeperserver.food.business.FoodService;
import com.foodkeeper.foodkeeperserver.food.controller.v1.request.FoodCursorRequest;
import com.foodkeeper.foodkeeperserver.food.controller.v1.request.FoodRegisterRequest;
import com.foodkeeper.foodkeeperserver.food.controller.v1.response.*;
import com.foodkeeper.foodkeeperserver.food.domain.response.RecipeFood;
import com.foodkeeper.foodkeeperserver.food.domain.RegisteredFood;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodsFinder;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodRegister;
import com.foodkeeper.foodkeeperserver.food.domain.response.FoodCursorResult;
import com.foodkeeper.foodkeeperserver.support.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

import java.util.List;

@Tag(name = "Food", description = "식재료 관련 API")
@RestController
@RequestMapping("/api/v1/foods")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @NullMarked
    @Operation(summary = "식재료 추가", description = "식재료 추가 API")
    @PostMapping
    public ResponseEntity<ApiResponse<FoodRegisterResponse>> createFood(@RequestPart @Valid FoodRegisterRequest request,
                                                                        @RequestPart(required = false) MultipartFile image) {
        String memberKey = "memberKey"; // todo 로그인 방식 구현 후 리팩토링
        FoodRegister register = FoodRegisterRequest.toRegister(request);
        Long foodId = foodService.registerFood(register, image, memberKey);
        return ResponseEntity.ok(ApiResponse.success(new FoodRegisterResponse(foodId)));
    }

    @NullMarked
    @Operation(summary = "식재료 즐겨찾기 추가", description = "식재료 즐겨찾기 추가 API")
    @PostMapping("/{foodId}/bookmark")
    public ResponseEntity<Void> bookmarkFood(@PathVariable Long foodId, String memberKey) { // TODO: 인증 객체 annot.추가
        Long bookmarkedFoodId = foodService.bookmarkFood(foodId, memberKey);
        return ResponseEntity.created(URI.create("/api/v1/bookmarked-foods/" + bookmarkedFoodId)).build();
    }

    @Operation(summary = "식재료 전체 조회", description = "식재료 전체 조회 API")
    @GetMapping
    public ResponseEntity<ApiResponse<FoodListResponse>> getFoods(@ModelAttribute FoodCursorRequest request) {
        String memberKey = "memberKey"; // todo 로그인 방식 구현 후 리팩토링
        FoodsFinder finder = FoodCursorRequest.toFinder(request, memberKey);
        FoodCursorResult result = foodService.getFoodList(finder);
        return ResponseEntity.ok(ApiResponse.success(new FoodListResponse(result)));
    }

    @Operation(summary = "식재료 단일 조회", description = "식재료 단일 조회 API")
    @GetMapping("/{foodId}")
    public ResponseEntity<ApiResponse<FoodResponse>> getFood(@PathVariable Long foodId) {
        String memberKey = "memberKey"; // todo 로그인 방식 구현 후 리팩토링
        RegisteredFood RegisteredFood = foodService.getFood(foodId, memberKey);
        return ResponseEntity.ok(ApiResponse.success(FoodResponse.toFoodResponse(RegisteredFood)));
    }

    @Operation(summary = "레시피 추천용 식재료 전체 조회", description = "레시피 추천용 식재료 전체 조회 API")
    @GetMapping("/recipes")
    public ResponseEntity<ApiResponse<RecipeFoodResponse>> getFoodNames() {
        String memberKey = "memberKey"; // todo 로그인 방식 구현 후 리팩토링
        List<RecipeFood> recipeFoods = foodService.getAllBymemberKey(memberKey);
        return ResponseEntity.ok(ApiResponse.success(new RecipeFoodResponse(recipeFoods)));
    }

    @Operation(summary = "유통기한 임박 식재료 리스트 조회", description = "유통기한 임박 식재료 조회 API")
    @GetMapping("/imminent")
    public ResponseEntity<ApiResponse<FoodImminentResponse>> getImminentFoods() {
        String memberKey = "memberKey"; // todo 로그인 방식 구현 후 리팩토링
        List<RecipeFood> foods = foodService.getImminentFoods(memberKey);
        return ResponseEntity.ok(ApiResponse.success(new FoodImminentResponse(foods)));
    }


}

