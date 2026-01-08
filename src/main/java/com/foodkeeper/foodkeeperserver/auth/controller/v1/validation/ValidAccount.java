package com.foodkeeper.foodkeeperserver.auth.controller.v1.validation;

import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@NotBlank
@Size(min = 6, max = 12)
@Pattern(regexp = "^[a-zA-Z0-9]+$")
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidAccount {
    String message() default "유효하지 않은 계정 형식입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}