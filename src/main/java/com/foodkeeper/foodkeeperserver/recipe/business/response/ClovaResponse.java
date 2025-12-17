package com.foodkeeper.foodkeeperserver.recipe.business.response;

public record ClovaResponse(ClovaResponseStatus status, ClovaResult result) {
    public String getContent() {
        if (result != null && result.message() != null) {
            return result.message().content();
        }
        return "레시피를 생성할 수 없습니다.";
    }
}
