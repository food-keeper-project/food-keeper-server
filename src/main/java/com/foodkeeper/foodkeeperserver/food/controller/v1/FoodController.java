package com.foodkeeper.foodkeeperserver.food.controller.v1;

import com.foodkeeper.foodkeeperserver.food.business.FoodService;
import com.foodkeeper.foodkeeperserver.food.dto.request.FoodRegisterRequest;
import com.foodkeeper.foodkeeperserver.support.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Food", description = "식재료 관련 API")
@RestController
@RequestMapping("/api/v1/foods")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @Operation(summary = "식재료 추가", description = "식재료 추가 API")
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createFood(@RequestPart FoodRegisterRequest request,
                                                     @RequestPart MultipartFile image){
        String memberId = "memberId"; // todo 로그인 방식 구현 후 리팩토링
        Long foodId = foodService.registerFood(request,image,memberId);
        return ResponseEntity.ok(ApiResponse.success(foodId));
    }


}
