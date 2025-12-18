package com.foodkeeper.foodkeeperserver.recipe.business;

import com.foodkeeper.foodkeeperserver.food.implement.FoodManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final ClovaService clovaService;
    private final FoodManager foodManager;


}
