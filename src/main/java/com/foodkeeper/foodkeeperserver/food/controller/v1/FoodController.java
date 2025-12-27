package com.foodkeeper.foodkeeperserver.food.controller.v1;

import com.foodkeeper.foodkeeperserver.common.controller.CursorDefault;
import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;
import com.foodkeeper.foodkeeperserver.food.business.FoodService;
import com.foodkeeper.foodkeeperserver.food.controller.v1.request.FoodRegisterRequest;
import com.foodkeeper.foodkeeperserver.food.controller.v1.request.FoodsRequest;
import com.foodkeeper.foodkeeperserver.food.controller.v1.response.FoodImminentResponse;
import com.foodkeeper.foodkeeperserver.food.controller.v1.response.FoodRegisterResponse;
import com.foodkeeper.foodkeeperserver.food.controller.v1.response.FoodResponse;
import com.foodkeeper.foodkeeperserver.food.controller.v1.response.RecipeFoodResponse;
import com.foodkeeper.foodkeeperserver.food.domain.RecipeFood;
import com.foodkeeper.foodkeeperserver.food.domain.RegisteredFood;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodRegister;
import com.foodkeeper.foodkeeperserver.member.domain.Member;
import com.foodkeeper.foodkeeperserver.security.auth.AuthMember;
import com.foodkeeper.foodkeeperserver.support.response.ApiResponse;
import com.foodkeeper.foodkeeperserver.support.response.PageResponse;
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
                                                                        @RequestPart(required = false) MultipartFile image,
                                                                        @AuthMember Member authMember) {
        FoodRegister register = FoodRegisterRequest.toRegister(request);
        Long foodId = foodService.registerFood(register, image, authMember.memberKey());
        return ResponseEntity.ok(ApiResponse.success(new FoodRegisterResponse(foodId)));
    }

    @NullMarked
    @Operation(summary = "식재료 즐겨찾기 추가", description = "식재료 즐겨찾기 추가 API")
    @PostMapping("/{foodId}/bookmark")
    public ResponseEntity<Void> bookmarkFood(@PathVariable Long foodId, @AuthMember Member authMember) {
        Long bookmarkedFoodId = foodService.bookmarkFood(foodId, authMember.memberKey());
        return ResponseEntity.created(URI.create("/api/v1/bookmarked-foods/" + bookmarkedFoodId)).build();
    }

    @Operation(summary = "식재료 전체 조회", description = "식재료 전체 조회 API")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<FoodResponse>>> getFoods(@ModelAttribute FoodsRequest request,
                                                                            @CursorDefault(defaultLimit = 10) Cursorable cursorable,
                                                                            @AuthMember Member authMember) {
        SliceObject<RegisteredFood> foods = foodService.getFoodList(cursorable, request.categoryId(), request.lastCreatedAt(), authMember.memberKey());
        List<FoodResponse> foodResponses = foods.getContent().stream().map(FoodResponse::toFoodResponse).toList();
        return ResponseEntity.ok(ApiResponse.success(new PageResponse<>(foodResponses, foods.isHasNext())));
    }

    @Operation(summary = "식재료 단일 조회", description = "식재료 단일 조회 API")
    @GetMapping("/{foodId}")
    public ResponseEntity<ApiResponse<FoodResponse>> getFood(@PathVariable Long foodId, @AuthMember Member authMember) {
        RegisteredFood RegisteredFood = foodService.getFood(foodId, authMember.memberKey());
        return ResponseEntity.ok(ApiResponse.success(FoodResponse.toFoodResponse(RegisteredFood)));
    }

    @Operation(summary = "레시피 추천용 식재료 전체 조회", description = "레시피 추천용 식재료 전체 조회 API")
    @GetMapping("/recipes")
    public ResponseEntity<ApiResponse<RecipeFoodResponse>> getFoodNames(@AuthMember Member authMember) {
        List<RecipeFood> recipeFoods = foodService.getAllByMemberKey(authMember.memberKey());
        return ResponseEntity.ok(ApiResponse.success(new RecipeFoodResponse(recipeFoods)));
    }

    @Operation(summary = "유통기한 임박 식재료 리스트 조회", description = "유통기한 임박 식재료 조회 API")
    @GetMapping("/imminent")
    public ResponseEntity<ApiResponse<FoodImminentResponse>> getImminentFoods(@AuthMember Member authMember) {
        List<RecipeFood> foods = foodService.getImminentFoods(authMember.memberKey());
        return ResponseEntity.ok(ApiResponse.success(new FoodImminentResponse(foods)));
    }


}

