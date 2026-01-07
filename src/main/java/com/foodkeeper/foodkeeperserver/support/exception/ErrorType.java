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
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, ErrorCode.E429, "너무 많은 요청을 보냈습니다.", LogLevel.WARN),

    // Food
    FOOD_DATA_NOT_FOUND(HttpStatus.BAD_REQUEST, ErrorCode.E1000, "해당 식재료가 존재하지 않습니다.", LogLevel.WARN),

    // Recipe
    RECIPE_DATA_NOT_FOUND(HttpStatus.BAD_REQUEST, ErrorCode.E2000, "해당 레시피가 존재하지 않습니다.", LogLevel.WARN),

    // Auth
    INVALID_OAUTH_USER(HttpStatus.BAD_REQUEST, ErrorCode.E3000, "존재하지 않는 OAuth 유저입니다.", LogLevel.WARN),
    MALFORMED_JWT(HttpStatus.BAD_REQUEST, ErrorCode.E3001, "JWT가 손상되었습니다.", LogLevel.WARN),
    UNSUPPORTED_JWT(HttpStatus.BAD_REQUEST, ErrorCode.E3002, "지원하지 않는 JWT 형식입니다.", LogLevel.WARN),
    EXPIRED_JWT(HttpStatus.BAD_REQUEST, ErrorCode.E3003, "JWT 기한이 만료되었습니다.", LogLevel.WARN),
    INVALID_SIGNATURE(HttpStatus.BAD_REQUEST, ErrorCode.E3004, "JWT Signature 검증에 실패했습니다.", LogLevel.WARN),
    INVALID_JWT(HttpStatus.BAD_REQUEST, ErrorCode.E3005, "JWT가 유효하지 않습니다.", LogLevel.WARN),
    NOT_FOUND_SUBJECT(HttpStatus.BAD_REQUEST, ErrorCode.E3006, "Subject를 찾을 수 없습니다.", LogLevel.WARN),
    OAUTH_ACCESS_TOKEN_IS_NULL(HttpStatus.BAD_REQUEST, ErrorCode.E3007, "OAuth 엑세스 토큰은 null일 수 없습니다.", LogLevel.WARN),
    FCM_TOKEN_IS_NULL(HttpStatus.BAD_REQUEST, ErrorCode.E3008, "FCM 토큰은 null일 수 없습니다.", LogLevel.WARN),
    ACCOUNT_IS_NULL(HttpStatus.BAD_REQUEST, ErrorCode.E3009, "계정은 null일 수 없습니다.", LogLevel.WARN),
    INVALID_ACCOUNT_LENGTH(HttpStatus.BAD_REQUEST, ErrorCode.E3010, "계정 길이가 너무 깁니다.", LogLevel.WARN),
    PASSWORD_IS_NULL(HttpStatus.BAD_REQUEST, ErrorCode.E3011, "비밀번호는 null일 수 없습니다.", LogLevel.WARN),
    INVALID_PASSWORD_LENGTH(HttpStatus.BAD_REQUEST, ErrorCode.E3012, "비밀번호 길이가 너무 깁니다.", LogLevel.WARN),
    NOT_VERIFIED_EMAIL(HttpStatus.BAD_REQUEST, ErrorCode.E3013, "이메일이 인증되지 않았습니다.", LogLevel.WARN),

    // S3
    S3_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E5000, "이미지 업로드에 실패했습니다.", LogLevel.ERROR),
    S3_DELETE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E5001, "이미지 삭제에 실패했습니다.", LogLevel.ERROR),

    // Naver Clova
    NAVER_CLOVA_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E6000, "네이버 클로바 AI 서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.", LogLevel.ERROR),
    NAVER_CLOVA_PROMPT_ERROR(HttpStatus.NOT_FOUND, ErrorCode.E6001, "실행할 프롬프트 업로드에 오류가 발생했습니다.", LogLevel.ERROR),

    // FCM
    FCM_TOKEN_INITIALIZE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E7000, "Firebase 초기화 중 오류가 발생하였습니다.", LogLevel.ERROR),
    FCM_TOKEN_SEND_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E7001, "알림 메시지 전송에 실패했습니다.", LogLevel.ERROR),

    // Food Category
    CATEGORY_DATA_NOT_FOUND(HttpStatus.BAD_REQUEST, ErrorCode.E8000, "존재하지 않는 카테고리 입니다.",LogLevel.WARN),

    // Member
    INVALID_MEMBER_KEY(HttpStatus.BAD_REQUEST, ErrorCode.E9000, "멤버 key가 유효하지 않습니다.", LogLevel.WARN),
    INVALID_NICKNAME_LENGTH(HttpStatus.BAD_REQUEST, ErrorCode.E9001, "닉네임이 너무 깁니다.", LogLevel.WARN),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, ErrorCode.E9002, "이메일이 유효하지 않습니다.", LogLevel.WARN),
    INVALID_IMAGE_URL(HttpStatus.BAD_REQUEST, ErrorCode.E9003, "이미지 URL이 유효하지 않습니다.", LogLevel.WARN),

    // Mail
    MAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E10000, "메일 전송에 실패했습니다.", LogLevel.ERROR),

    // Email Verification
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, ErrorCode.E10001, "이미 존재하는 이메일입니다.", LogLevel.WARN),
    TOO_MUCH_FAILED(HttpStatus.BAD_REQUEST, ErrorCode.E10002, "너무 많이 실패했습니다 다시 시도해주세요.", LogLevel.WARN),
    INVALID_EMAIL_CODE(HttpStatus.BAD_REQUEST, ErrorCode.E10003, "유효하지 않은 인증 코드입니다.", LogLevel.WARN),
    EXPIRED_EMAIL_CODE(HttpStatus.BAD_REQUEST, ErrorCode.E10004, "만료된 인증 코드입니다.", LogLevel.WARN),
    ;

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
