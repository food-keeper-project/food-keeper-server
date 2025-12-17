package com.foodkeeper.foodkeeperserver.recipe.business;

import com.foodkeeper.foodkeeperserver.food.implement.FoodManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final ClovaService clovaService;
    private final FoodManager foodManager;

    // 유통기한 임박한 식재료 기반 AI 호출


    // 전체 식재료 기반 AI 호출
}
