package com.foodkeeper.foodkeeperserver.infra.ai.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record ClovaResponse(ClovaResponseStatus status, ClovaResult result) {
    private static final String EMPTY_JSON = "{}";
    private static final String JSON_REGEX = "(\\{.*\\}|\\[.*\\])";
    public static final String JSON_EMPTY_MESSAGE = "응답을 생성할 수 없습니다.";
    public static final String NOT_JSON_ELEMENTS = "(?s)```(?:json)?\\s*(.*?)\\s*```";
    public static final String WORD_WITHOUT_BRACKET = "(?<![\"'])(\\w+)\\s*:";

    public String getContent() {
        if (result != null && result.message() != null) {
            return cleanJsonContent(result.message().content());
        }
        return JSON_EMPTY_MESSAGE;
    }

    // 마크다운 형식 제거 -> JSON 형식으로
    private String cleanJsonContent(String rawContent) {
        if (rawContent == null) return EMPTY_JSON;

        String content = rawContent.replaceAll(NOT_JSON_ELEMENTS, "$1");
        content = content.replaceAll(WORD_WITHOUT_BRACKET, "\"$1\":");

        Matcher matcher = Pattern.compile(JSON_REGEX, Pattern.DOTALL).matcher(content);
        if (matcher.find()) {
            return matcher.group().trim();
        }

        return EMPTY_JSON;
    }
}
