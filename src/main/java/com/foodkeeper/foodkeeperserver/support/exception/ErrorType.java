package com.foodkeeper.foodkeeperserver.support.exception;

import lombok.Getter;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {
    DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E500, "알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도해주세요.", LogLevel.ERROR),
    REQUIRED_AUTH(HttpStatus.UNAUTHORIZED, ErrorCode.E401, "리소스에 접근하기 위한 인증이 필요합니다.", LogLevel.WARN),
    FAILED_AUTH(HttpStatus.BAD_REQUEST, ErrorCode.E400, "인증에 실패했습니다.", LogLevel.WARN),
    INVALID_ACCESS_PATH(HttpStatus.BAD_REQUEST, ErrorCode.E400, "잘못된 접근 경로입니다.", LogLevel.WARN),
    NOT_FOUND_DATA(HttpStatus.BAD_REQUEST, ErrorCode.E400, "해당 데이터를 찾을 수 없습니다.", LogLevel.WARN),

    INVALID_OAUTH_USER(HttpStatus.BAD_REQUEST, ErrorCode.E3000, "존재하지 않는 OAuth 유저입니다.", LogLevel.WARN),
    MALFORMED_JWT(HttpStatus.BAD_REQUEST, ErrorCode.E3001, "JWT가 손상되었습니다.", LogLevel.WARN),
    UNSUPPORTED_JWT(HttpStatus.BAD_REQUEST, ErrorCode.E3002, "지원하지 않는 JWT 형식입니다.", LogLevel.WARN),
    EXPIRED_JWT(HttpStatus.BAD_REQUEST, ErrorCode.E3003, "JWT 기한이 만료되었습니다.", LogLevel.WARN),
    INVALID_SIGNATURE(HttpStatus.BAD_REQUEST, ErrorCode.E3004, "JWT Signature 검증에 실패했습니다.", LogLevel.WARN),
    INVALID_JWT(HttpStatus.BAD_REQUEST, ErrorCode.E3005, "JWT가 유효하지 않습니다.", LogLevel.WARN),
    NOT_FOUND_SUBJECT(HttpStatus.BAD_REQUEST, ErrorCode.E3006, "Subject를 찾을 수 없습니다.", LogLevel.WARN),
    OAUTH_ACCESS_TOKEN_IS_NULL(HttpStatus.BAD_REQUEST, ErrorCode.E3007, "OAuth 엑세스 토큰은 null일 수 없습니다.", LogLevel.WARN),
    FCM_TOKEN_IS_NULL(HttpStatus.BAD_REQUEST, ErrorCode.E3008, "FCM 토큰은 null일 수 없습니다.", LogLevel.WARN),

    // 식재료
    FOOD_DATA_NOT_FOUND(HttpStatus.BAD_REQUEST, ErrorCode.E1000, "해당 식재료가 존재하지 않습니다.", LogLevel.WARN),
    CATEGORY_SELECT_ERROR(HttpStatus.BAD_REQUEST, ErrorCode.E1001, "카테고리 선택 개수가 옳바르지 않습니다.", LogLevel.WARN),

    S3_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E5000, "이미지 업로드에 실패했습니다.", LogLevel.ERROR),

    // 네이버
    NAVER_CLOVA_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E6000, "네이버 클로바 AI 서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.", LogLevel.ERROR),
    NAVER_CLOVA_PROMPT_ERROR(HttpStatus.NOT_FOUND, ErrorCode.E6001, "실행할 프롬프트 업로드에 오류가 발생했습니다.", LogLevel.ERROR),


    INVALID_MEMBER_KEY(HttpStatus.BAD_REQUEST, ErrorCode.E9000, "멤버 key가 유효하지 않습니다.", LogLevel.WARN),
    INVALID_NICKNAME_LENGTH(HttpStatus.BAD_REQUEST, ErrorCode.E9001, "멤버 닉네임은 20자를 넘을 수 없습니다.", LogLevel.WARN),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, ErrorCode.E9002, "이메일이 유효하지 않습니다.", LogLevel.WARN),
    INVALID_IMAGE_URL(HttpStatus.BAD_REQUEST, ErrorCode.E9003, "이미지 URL이 유효하지 않습니다.", LogLevel.WARN),

    FCM_TOKEN_INITIALIZE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E7000, "Firebase 초기화 중 오류가 발생하였습니다.", LogLevel.ERROR),
    FCM_TOKEN_SEND_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E7001, "알림 메시지 전송에 실패했습니다.", LogLevel.ERROR);

    private final HttpStatus status;
    private final ErrorCode errorCode;
    private final String message;
    private final LogLevel logLevel;

    ErrorType(HttpStatus status, ErrorCode errorCode, String message, LogLevel logLevel) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
        this.logLevel = logLevel;
    }
}
