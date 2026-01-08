package com.foodkeeper.foodkeeperserver.auth.controller.v1.validation;

import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@NotBlank
@Size(min = 8, max = 20)
@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z0-9]+$")
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidPassword {
    String message() default "유효하지 않은 비밀번호 형식입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
