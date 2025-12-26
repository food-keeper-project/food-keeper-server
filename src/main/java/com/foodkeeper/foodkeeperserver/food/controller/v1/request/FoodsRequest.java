package com.foodkeeper.foodkeeperserver.food.controller.v1.request;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record FoodsRequest(Long categoryId) {
}
