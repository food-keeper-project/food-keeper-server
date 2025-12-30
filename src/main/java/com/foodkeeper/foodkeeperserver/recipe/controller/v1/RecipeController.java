package com.foodkeeper.foodkeeperserver.recipe.controller.v1;

import com.foodkeeper.foodkeeperserver.member.domain.Member;
import com.foodkeeper.foodkeeperserver.recipe.business.RecipeService;
import com.foodkeeper.foodkeeperserver.recipe.controller.v1.request.RecipeRegisterRequest;
import com.foodkeeper.foodkeeperserver.recipe.controller.v1.response.RecipeResponse;
import com.foodkeeper.foodkeeperserver.security.auth.AuthMember;
import com.foodkeeper.foodkeeperserver.support.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    @NullMarked
    @Operation(summary = "AI 레시피 추천", description = "AI 레시피 추천 API")
    @GetMapping("/recommend")
    public ResponseEntity<ApiResponse<RecipeResponse>> recommendRecipe(@RequestParam List<String> ingredients,
                                                                       @RequestParam List<String> excludedMenus) {
        return ResponseEntity.ok(ApiResponse.success(RecipeResponse.from(
                recipeService.recommendRecipe(ingredients, excludedMenus))));
    }

    @NullMarked
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> registerRecipe(@RequestBody @Valid RecipeRegisterRequest request,
                                                            @AuthMember Member member) {
        Long recipeId = recipeService.registerRecipe(request.toRecipe(), member.memberKey());
        return ResponseEntity.created(URI.create("/api/v1/recipes/" + recipeId)).body(ApiResponse.success());
    }
}
