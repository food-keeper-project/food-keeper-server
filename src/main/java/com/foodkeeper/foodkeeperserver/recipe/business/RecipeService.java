package com.foodkeeper.foodkeeperserver.recipe.business;

import com.foodkeeper.foodkeeperserver.recipe.controller.v1.response.RecipeResponse;
import com.foodkeeper.foodkeeperserver.recipe.implement.ClovaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final ClovaService clovaService;
    private final ObjectMapper objectMapper;


    public RecipeResponse recommendRecipe(List<String> ingredients) throws IOException {
        String response = clovaService.getRecipeRecommendation(ingredients);
        return objectMapper.readValue(response, RecipeResponse.class);
    }
}
