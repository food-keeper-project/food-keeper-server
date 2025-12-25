package com.foodkeeper.foodkeeperserver.recipe.controller.v1;


import com.foodkeeper.foodkeeperserver.recipe.business.request.ClovaRequest;
import com.foodkeeper.foodkeeperserver.recipe.business.response.ClovaResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface ClovaClient {
    @PostExchange
    ClovaResponse getRecipe(
            @RequestHeader("Authorization") String apiKey,
            @RequestBody ClovaRequest request
            );
}
