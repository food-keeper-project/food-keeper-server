package com.foodkeeper.foodkeeperserver.food.controller.v1;

import com.foodkeeper.foodkeeperserver.food.business.FoodService;
import com.foodkeeper.foodkeeperserver.food.controller.v1.request.FoodCursorRequest;
import com.foodkeeper.foodkeeperserver.food.controller.v1.request.FoodRegisterRequest;
import com.foodkeeper.foodkeeperserver.food.controller.v1.response.*;
import com.foodkeeper.foodkeeperserver.food.domain.MyFood;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodCursorFinder;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodRegister;
import com.foodkeeper.foodkeeperserver.food.domain.response.FoodCursorResult;
import com.foodkeeper.foodkeeperserver.food.domain.response.ImminentFood;
import com.foodkeeper.foodkeeperserver.support.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
                                                                        @RequestPart(required = false) MultipartFile image){
        String memberId = "memberId"; // todo 로그인 방식 구현 후 리팩토링
        FoodRegister register = FoodRegisterRequest.toRegister(request);
        Long foodId = foodService.registerFood(register, image, memberId);
        return ResponseEntity.ok(ApiResponse.success(new FoodRegisterResponse(foodId)));
    }

    @Operation(summary = "식재료 전체 조회", description = "식재료 전체 조회 API")
    @GetMapping
    public ResponseEntity<ApiResponse<FoodListResponse>> getFoods(@ModelAttribute FoodCursorRequest request) {
        String memberId = "memberId"; // todo 로그인 방식 구현 후 리팩토링
        FoodCursorFinder finder = FoodCursorRequest.toFinder(request, memberId);
        FoodCursorResult result = foodService.getFoodList(finder);
        return ResponseEntity.ok(ApiResponse.success(new FoodListResponse(result.foods(), result.hasNext())));
    }

    @Operation(summary = "식재료 단일 조회", description = "식재료 단일 조회 API")
    @GetMapping("/{foodId}")
    public ResponseEntity<ApiResponse<FoodResponse>> getFood(@PathVariable Long foodId) {
        String memberId = "memberId"; // todo 로그인 방식 구현 후 리팩토링
        MyFood myFood = foodService.getFood(foodId,memberId);
        return ResponseEntity.ok(ApiResponse.success(FoodResponse.toFoodResponse(myFood)));
    }
    // 이름만 반환하는 게 아니라 id, name, remainDays 를 반환해야될듯?
    @Operation(summary = "식재료 AI 이름 조회", description = "식재료 AI 이름 조회 API")
    @GetMapping("/recommend")
    public ResponseEntity<ApiResponse<FoodNameResponse>> getFoodNames(@RequestParam List<Long> ids) {
        String memberId = "memberId"; // todo 로그인 방식 구현 후 리팩토링
        List<String> names = foodService.getFoodNames(ids,memberId);
        return ResponseEntity.ok(ApiResponse.success(new FoodNameResponse(names)));
    }

    @Operation(summary = "유통기한 임박 식재료 리스트 조회", description = "유통기한 임박 식재료 조회 API")
    @GetMapping("/imminent")
    public ResponseEntity<ApiResponse<FoodImminentResponse>> getImminentFoods() {
        String memberId = "memberId"; // todo 로그인 방식 구현 후 리팩토링
        List<ImminentFood> foods = foodService.getImminentFoods(memberId);
        return ResponseEntity.ok(ApiResponse.success(new FoodImminentResponse(foods)));
    }
}
