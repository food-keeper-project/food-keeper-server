package com.foodkeeper.foodkeeperserver.recipe.business.response;

public record ClovaResponse(ClovaResponseStatus status, ClovaResult result) {
    private static final String EMPTY_JSON = "{}";
    public String getContent() {
        if (result != null && result.message() != null) {
            return cleanJsonContent(result.message().content());
        }
        return "레시피를 생성할 수 없습니다.";
    }

    // 마크다운 형식 제거 -> JSON 형식으로
    private String cleanJsonContent(String rawContent) {
        if (rawContent == null) return EMPTY_JSON;

        String cleanedJson = rawContent.trim();

        int start = cleanedJson.indexOf("{");
        int end = cleanedJson.lastIndexOf("}");

        if (start != -1 && end != -1 && start < end) {
            return cleanedJson.substring(start, end + 1);
        }

        return cleanedJson;
    }
}
