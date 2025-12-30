package com.foodkeeper.foodkeeperserver.recipe.controller.v1;

import com.foodkeeper.foodkeeperserver.recipe.business.RecipeService;
import com.foodkeeper.foodkeeperserver.recipe.controller.v1.response.RecipeResponse;
import com.foodkeeper.foodkeeperserver.support.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
