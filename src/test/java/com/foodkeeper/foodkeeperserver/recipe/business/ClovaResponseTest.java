package com.foodkeeper.foodkeeperserver.recipe.business;

import com.foodkeeper.foodkeeperserver.infra.ai.domain.ClovaMessage;
import com.foodkeeper.foodkeeperserver.infra.ai.domain.AiType;
import com.foodkeeper.foodkeeperserver.infra.ai.domain.ClovaResponse;
import com.foodkeeper.foodkeeperserver.infra.ai.domain.ClovaResponseStatus;
import com.foodkeeper.foodkeeperserver.infra.ai.domain.ClovaResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClovaResponseTest {

    @Test
    @DisplayName("Clova 응답 값을 Json 형식으로 추출한다.")
    void clovaResponseConvertToJson() {
        ClovaResponseStatus status = new ClovaResponseStatus("code", "message");
        ClovaMessage message = new ClovaMessage(
                AiType.ASSISTANT,
                "## dfsdvi --- ## { 'recipe': { 'test' : 'test' } } ## adsfwe ## sdafwe");
        ClovaResult result = new ClovaResult(message);
        ClovaResponse response = new ClovaResponse(status, result);

        assertThat(response.getContent()).isEqualTo("{ 'recipe': { 'test' : 'test' } }");
    }
}