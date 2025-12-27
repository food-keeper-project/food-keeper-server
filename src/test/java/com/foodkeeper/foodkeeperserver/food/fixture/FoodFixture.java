package com.foodkeeper.foodkeeperserver.food.fixture;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.domain.StorageMethod;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodsFinder;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodRegister;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class FoodFixture {

    public static final Long ID = 1L;
    public static final String NAME = "우유";
    public static final String MEMO = "우유 마시쪙";
    public static final StorageMethod STORAGE_METHOD = StorageMethod.FROZEN;
    public static final LocalDate EXPIRY_DATE = LocalDate.now().plusDays(1);
    public static final Integer EXPIRY_ALARM = 3;
    public static final String IMAGE_URL = "https://s3.aws.com/milk.jpg";
    public static final String MEMBER_KEY = "memberKey";


    public static FoodRegister createRegisterDto(List<Long> categoryIds) {
        return new FoodRegister(
                NAME,
                categoryIds,
                STORAGE_METHOD,
                EXPIRY_DATE,
                3,
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
                .memberKey(MEMBER_KEY)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static FoodEntity createFoodEntity(Long id) {
        FoodEntity foodEntity = FoodEntity.from(createFood());
        ReflectionTestUtils.setField(foodEntity, "id", id);
        return foodEntity;
    }
}
