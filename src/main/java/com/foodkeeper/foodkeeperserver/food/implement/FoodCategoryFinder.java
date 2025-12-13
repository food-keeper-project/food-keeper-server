package com.foodkeeper.foodkeeperserver.food.implement;

import com.foodkeeper.foodkeeperserver.common.utils.ListUtil;
import com.foodkeeper.foodkeeperserver.food.dto.request.FoodRegisterRequest;
import com.foodkeeper.foodkeeperserver.food.entity.FoodCategory;
import com.foodkeeper.foodkeeperserver.food.repository.FoodCategoryRepository;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FoodCategoryFinder {

    private final FoodCategoryRepository foodCategoryRepository;

    // 카테고리 먼저 조회
    @Transactional(readOnly = true)
    public List<FoodCategory> findAll(List<Long> categoryIds) {
        return ListUtil.getOrElseThrowList(foodCategoryRepository.findAllById(categoryIds));
    }

}
