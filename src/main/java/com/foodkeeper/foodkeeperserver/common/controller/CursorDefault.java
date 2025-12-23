package com.foodkeeper.foodkeeperserver.common.controller;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.PARAMETER)
public @interface CursorDefault {
    int defaultLimit() default 10;
}
