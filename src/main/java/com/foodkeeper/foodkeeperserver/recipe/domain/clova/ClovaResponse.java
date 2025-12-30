package com.foodkeeper.foodkeeperserver.recipe.domain.clova;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record ClovaResponse(ClovaResponseStatus status, ClovaResult result) {
    private static final String EMPTY_JSON = "{}";
    private static final String JSON_REGEX = "(\\{.*\\}|\\[.*\\])";
    public static final String JSON_EMPTY_MESSAGE = "레시피를 생성할 수 없습니다.";

    public String getContent() {
        if (result != null && result.message() != null) {
            return cleanJsonContent(result.message().content());
        }
        return JSON_EMPTY_MESSAGE;
    }

    // 마크다운 형식 제거 -> JSON 형식으로
    private String cleanJsonContent(String rawContent) {
        if (rawContent == null) return EMPTY_JSON;

        Matcher matcher = Pattern.compile(JSON_REGEX, Pattern.DOTALL).matcher(rawContent);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return EMPTY_JSON;
    }
}
