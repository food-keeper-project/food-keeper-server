package com.foodkeeper.foodkeeperserver.food.fixture;

import com.foodkeeper.foodkeeperserver.food.business.request.FoodRegisterDto;
import com.foodkeeper.foodkeeperserver.food.controller.v1.request.FoodRegisterRequest;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.domain.StorageMethod;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;

public class FoodFixture {

    public static final Long ID = 1L;
    public static final String NAME = "우유";
    public static final String MEMO = "우유 마시쪙";
    public static final StorageMethod STORAGE_METHOD = StorageMethod.FROZEN;
    public static final LocalDate EXPIRY_DATE = LocalDate.now().plusDays(1);
    public static final String IMAGE_URL = "https://s3.aws.com/milk.jpg";
    public static final String MEMBER_ID = "memberId";


    public static FoodRegisterDto createRegisterDto(List<Long> categoryIds) {
        return new FoodRegisterDto(
                NAME,
                categoryIds,
                STORAGE_METHOD,
                EXPIRY_DATE,
                MEMO
        );
    }
    public static Food createFood() {
        return Food.builder()
                .id(ID)
                .name(NAME)
                .imageUrl(IMAGE_URL)
                .storageMethod(STORAGE_METHOD)
                .expiryDate(EXPIRY_DATE)
                .memo(MEMO)
                .selectedCategoryCount(1)
                .memberId(MEMBER_ID)
                .build();
    }

    public static FoodEntity createFoodEntity() {
        FoodEntity foodEntity = FoodEntity.from(createFood());
        ReflectionTestUtils.setField(foodEntity,"id",1L);
        return foodEntity;
    }
}
