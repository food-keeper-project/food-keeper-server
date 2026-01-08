package com.foodkeeper.foodkeeperserver.auth.enums;

import lombok.Getter;

@Getter
public enum VerificationMessageTitle {
    SIGN_UP("[키친로그] 회원가입 인증 번호입니다."),
    RECOVER_ACCOUNT("[키친로그] 아이디 찾기 인증 번호입니다."),
    RECOVER_PASSWORD("[키친로그] 비밀번호 찾기 인증 번호입니다."),
    ;
    private String title;

    VerificationMessageTitle(String title) {
        this.title = title;
    }
}
