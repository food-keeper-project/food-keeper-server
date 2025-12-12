package com.foodkeeper.foodkeeperserver.domain.food.business;

import com.foodkeeper.foodkeeperserver.domain.food.dto.request.FoodRegisterRequest;
import com.foodkeeper.foodkeeperserver.domain.food.entity.Food;
import com.foodkeeper.foodkeeperserver.domain.food.implement.FoodCreator;
import com.foodkeeper.foodkeeperserver.domain.food.implement.ImageUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final ImageUploader imageUploader;
    private final FoodCreator foodCreator;



    // 이름,사진,카테고리(최대 3개), 유통기한,유통기한 알림 설정,메모,보관 방식
//    public Long registerFood(FoodRegisterRequest request, String memberId) {
//
//        Food food = FoodRegisterRequest.toEntity(request,url,memberId);
//    }
}
