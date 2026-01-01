package com.foodkeeper.foodkeeperserver.food.controller.v1;

import com.foodkeeper.foodkeeperserver.common.controller.CursorDefault;
import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;
import com.foodkeeper.foodkeeperserver.food.business.FoodService;
import com.foodkeeper.foodkeeperserver.food.controller.v1.request.FoodRegisterRequest;
import com.foodkeeper.foodkeeperserver.food.controller.v1.request.FoodsRequest;
import com.foodkeeper.foodkeeperserver.food.controller.v1.response.FoodResponses;
import com.foodkeeper.foodkeeperserver.food.controller.v1.response.FoodIdResponse;
import com.foodkeeper.foodkeeperserver.food.controller.v1.response.FoodResponse;
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
    public ResponseEntity<ApiResponse<Void>> createFood(@RequestPart @Valid FoodRegisterRequest request,
                                                                  @RequestPart(required = false) MultipartFile image,
                                                                  @AuthMember Member authMember) {
        FoodRegister register = FoodRegisterRequest.toRegister(request);
        Long foodId = foodService.registerFood(register, image, authMember.memberKey());
        return ResponseEntity.created(URI.create("/api/v1/foods" + foodId)).build();
    }

    @NullMarked
    @Operation(summary = "식재료 즐겨찾기 추가", description = "식재료 즐겨찾기 추가 API")
    @PostMapping("/{foodId}/bookmark")
    public ResponseEntity<Void> bookmarkFood(@PathVariable Long foodId, @AuthMember Member authMember) {
        Long bookmarkedFoodId = foodService.bookmarkFood(foodId, authMember.memberKey());
        return ResponseEntity.created(URI.create("/api/v1/bookmarked-foods/" + bookmarkedFoodId)).build();
    }

    @NullMarked
    @Operation(summary = "식재료 전체 조회", description = "식재료 전체 조회 API")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<FoodResponse>>> findFoods(@ModelAttribute FoodsRequest request,
                                                                            @CursorDefault Cursorable<Long> cursorable,
                                                                            @AuthMember Member authMember) {
        SliceObject<RegisteredFood> foods = foodService.findFoodList(cursorable, request.categoryId(), authMember.memberKey());
        List<FoodResponse> foodResponses = foods.content().stream().map(FoodResponse::toFoodResponse).toList();
        return ResponseEntity.ok(ApiResponse.success(new PageResponse<>(foodResponses, foods.hasNext())));
    }

    @NullMarked
    @Operation(summary = "식재료 단일 조회", description = "식재료 단일 조회 API")
    @GetMapping("/{foodId}")
    public ResponseEntity<ApiResponse<FoodResponse>> findFood(@PathVariable Long foodId, @AuthMember Member authMember) {
        RegisteredFood registeredFood = foodService.findFood(foodId, authMember.memberKey());
        return ResponseEntity.ok(ApiResponse.success(FoodResponse.toFoodResponse(registeredFood)));
    }

    @NullMarked
    @Operation(summary = "레시피 추천용 식재료 전체 조회", description = "레시피 추천용 식재료 전체 조회 API")
    @GetMapping("/recipes")
    public ResponseEntity<ApiResponse<FoodResponses>> findFoodNames(@AuthMember Member authMember) {
        List<RegisteredFood> recipeFoods = foodService.findAllFoods(authMember.memberKey());
        return ResponseEntity.ok(ApiResponse.success(new FoodResponses(FoodResponse.toFoodListResponse(recipeFoods))));
    }

    @NullMarked
    @Operation(summary = "유통기한 임박 식재료 리스트 조회", description = "유통기한 임박 식재료 조회 API")
    @GetMapping("/imminent")
    public ResponseEntity<ApiResponse<FoodResponses>> findImminentFoods() {
        List<RegisteredFood> imminentFoods = foodService.findImminentFoods("membre");
        return ResponseEntity.ok(ApiResponse.success(new FoodResponses(FoodResponse.toFoodListResponse(imminentFoods))));
    }

    @NullMarked
    @Operation(summary = "식재료 소비완료", description = "식재료 소비완료 API")
    @DeleteMapping("/{foodId}")
    public ResponseEntity<ApiResponse<Void>> removeFood(@PathVariable Long foodId, @AuthMember Member member) {
        Long resultId = foodService.removeFood(foodId, member.memberKey());
        return ResponseEntity.created(URI.create("/api/v1/foods" + resultId)).build();
    }
}

