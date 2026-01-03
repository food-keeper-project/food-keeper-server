package com.foodkeeper.foodkeeperserver.support.validation;

import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.lang.annotation.*;

@NotBlank
@Email(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MemberEmail {
    String message() default "유효하지 않은 이메일입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
