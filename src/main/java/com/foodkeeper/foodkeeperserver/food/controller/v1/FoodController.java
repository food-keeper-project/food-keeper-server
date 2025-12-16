package com.foodkeeper.foodkeeperserver.food.controller.v1;

import com.foodkeeper.foodkeeperserver.food.business.FoodService;
import com.foodkeeper.foodkeeperserver.food.controller.v1.request.FoodRegisterRequest;
import com.foodkeeper.foodkeeperserver.food.controller.v1.response.FoodRegisterResponse;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodRegister;
import com.foodkeeper.foodkeeperserver.support.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Food", description = "식재료 관련 API")
@RestController
@RequestMapping("/api/v1/foods")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @Operation(summary = "식재료 추가", description = "식재료 추가 API")
    @PostMapping
    public ResponseEntity<ApiResponse<FoodRegisterResponse>> createFood(@RequestPart @Valid FoodRegisterRequest request,
                                                                        @RequestPart(required = false) MultipartFile image) {
        String memberId = "memberId"; // todo 로그인 방식 구현 후 리팩토링
        FoodRegister register = FoodRegisterRequest.toRegister(request);
        Long foodId = foodService.registerFood(register, image, memberId);
        return ResponseEntity.ok(ApiResponse.success(new FoodRegisterResponse(foodId)));
    }


}
