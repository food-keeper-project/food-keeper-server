package com.foodkeeper.foodkeeperserver.food.controller.v1;


import com.foodkeeper.foodkeeperserver.food.business.FoodCategoryService;
import com.foodkeeper.foodkeeperserver.food.controller.v1.request.FoodCategoryRegisterRequest;
import com.foodkeeper.foodkeeperserver.food.controller.v1.request.UpdateCategoryRequest;
import com.foodkeeper.foodkeeperserver.food.controller.v1.response.FoodCategoryResponse;
import com.foodkeeper.foodkeeperserver.food.domain.FoodCategory;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodCategoryRegister;
import com.foodkeeper.foodkeeperserver.member.domain.Member;
import com.foodkeeper.foodkeeperserver.security.auth.AuthMember;
import com.foodkeeper.foodkeeperserver.support.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    public ResponseEntity<ApiResponse<Void>> createCategory(@RequestBody FoodCategoryRegisterRequest request,
                                                            @AuthMember Member member) {
        FoodCategoryRegister register = new FoodCategoryRegister(request.name(), member.memberKey());
        foodCategoryService.registerFoodCategory(register);
        return ResponseEntity.created(URI.create("/api/v1/categories")).build();
    }

    @NullMarked
    @Operation(summary = "카테고리 전체 조회", description = "카테고리 전체 조회 API")
    @GetMapping
    public ResponseEntity<ApiResponse<List<FoodCategoryResponse>>> getCategories(@AuthMember Member member) {
        List<FoodCategory> foodCategories = foodCategoryService.findAllByMemberKey(member.memberKey());
        List<FoodCategoryResponse> responses = foodCategories.stream()
                .map(FoodCategoryResponse::from)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @NullMarked
    @Operation(summary = "카테고리 이름 수정", description = "카테고리 이름 수정 API")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> update(@PathVariable Long id, @RequestBody @Valid UpdateCategoryRequest request, @AuthMember Member member) {
        foodCategoryService.updateCategory(id, request.name(), member.memberKey());
        return ResponseEntity.ok(ApiResponse.success());
    }

    @NullMarked
    @Operation(summary = "카테고리 삭제", description = "카테고리 삭제 API")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> remove(@PathVariable Long id, @AuthMember Member member) {
        foodCategoryService.removeCategory(id, member.memberKey());
        return ResponseEntity.ok(ApiResponse.success());
    }
}
