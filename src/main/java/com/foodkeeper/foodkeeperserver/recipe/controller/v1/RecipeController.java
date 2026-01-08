package com.foodkeeper.foodkeeperserver.recipe.controller.v1;

import com.foodkeeper.foodkeeperserver.common.controller.CursorDefault;
import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.member.domain.Member;
import com.foodkeeper.foodkeeperserver.recipe.business.RecipeService;
import com.foodkeeper.foodkeeperserver.recipe.controller.v1.request.RecipeRegisterRequest;
import com.foodkeeper.foodkeeperserver.recipe.controller.v1.response.RecipeCountResponse;
import com.foodkeeper.foodkeeperserver.recipe.controller.v1.response.RecipeListResponse;
import com.foodkeeper.foodkeeperserver.recipe.controller.v1.response.RecipeResponse;
import com.foodkeeper.foodkeeperserver.security.auth.AuthMember;
import com.foodkeeper.foodkeeperserver.support.response.ApiResponse;
import com.foodkeeper.foodkeeperserver.support.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Validated
@Tag(name = "Recipe", description = "레시피 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    @NullMarked
    @Operation(summary = "AI 레시피 추천", description = "AI 레시피 추천 API")
    @GetMapping("/recommend")
    public ResponseEntity<ApiResponse<RecipeResponse>> recommendRecipe(@Size(min = 1) @RequestParam List<String> ingredients,
                                                                       @RequestParam List<String> excludedMenus) {
        return ResponseEntity.ok(ApiResponse.success(RecipeResponse.from(
                recipeService.recommendRecipe(ingredients, excludedMenus))));
    }

    @NullMarked
    @Operation(summary = "레시피 등록", description = "레시피 등록 API")
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> registerRecipe(@Valid @RequestBody RecipeRegisterRequest request,
                                                            @AuthMember Member authMember) {
        Long recipeId = recipeService.registerRecipe(request.toNewRecipe(), authMember.memberKey());
        return ResponseEntity.created(URI.create("/api/v1/recipes/" + recipeId)).body(ApiResponse.success());
    }

    @NullMarked
    @Operation(summary = "레시피 목록 조회", description = "레시피 목록 조회 API")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<RecipeListResponse>>> findRecipes(@CursorDefault Cursorable<Long> cursorable,
                                                                                     @AuthMember Member authMember) {
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(
                recipeService.findRecipes(cursorable, authMember.memberKey())
                        .map(RecipeListResponse::from))));
    }

    @NullMarked
    @Operation(summary = "나의 레시피 개수 조회", description = "나의 레시피 개수 조회 API")
    @GetMapping("/count/me")
    public ResponseEntity<ApiResponse<RecipeCountResponse>> findRecipeCount(@AuthMember Member authMember) {
        return ResponseEntity.ok(ApiResponse.success(
                new RecipeCountResponse(recipeService.recipeCount(authMember.memberKey()))));
    }

    @NullMarked
    @Operation(summary = "레시피 단일 조회", description = "레시피 단일 조회 API")
    @GetMapping("/{recipeId}")
    public ResponseEntity<ApiResponse<RecipeResponse>> findRecipe(@PathVariable Long recipeId,
                                                                  @AuthMember Member authMember) {
        return ResponseEntity.ok(ApiResponse.success(RecipeResponse.from(
                recipeService.findRecipe(recipeId, authMember.memberKey()))));
    }

    @NullMarked
    @Operation(summary = "레시피 삭제", description = "레시피 삭제 API")
    @DeleteMapping("/{recipeId}")
    public ResponseEntity<ApiResponse<Void>> removeRecipe(@PathVariable Long recipeId,
                                                          @AuthMember Member authMember) {
        recipeService.removeRecipe(recipeId, authMember.memberKey());
        return ResponseEntity.ok(ApiResponse.success());
    }
}
