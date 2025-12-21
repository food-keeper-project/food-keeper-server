package com.foodkeeper.foodkeeperserver.support.exception;

public enum ErrorCode {
    E400,
    E401,
    E403,
    E404,
    E500,

    E1000, // 식재료 관련 에러
    E2000, // 카테고리 관련 에러
    E3000, // 레시피

    E5000, // S3 관련 에러
    E6000,
    E6001
}
