package com.foodkeeper.foodkeeperserver.support.exception;

public enum ErrorCode {
    E400,
    E401,
    E403,
    E404,
    E500,

    E1000, // 식재료 관련 에러

    E2000, // 레시피 관련 에러

    E3000, E3001, E3002, E3003, E3004, E3005, E3006, E3007, E3008, E3009, E3010, E3011, E3012,

    E5000, // S3 관련 에러
    E6000,
    E6001,

    E9000,
    E9001,
    E9002,
    E9003,
    E9004,

}
