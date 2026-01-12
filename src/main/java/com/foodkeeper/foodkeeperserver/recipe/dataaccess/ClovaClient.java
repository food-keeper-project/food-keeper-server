package com.foodkeeper.foodkeeperserver.recipe.dataaccess;

import com.foodkeeper.foodkeeperserver.infra.ai.domain.ClovaRequest;
import com.foodkeeper.foodkeeperserver.infra.ai.domain.ClovaResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface ClovaClient {
    @PostExchange
    ClovaResponse getAiResponse(
            @RequestHeader("Authorization") String apiKey,
            @RequestBody ClovaRequest request
    );
}
