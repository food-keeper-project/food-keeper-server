package com.foodkeeper.foodkeeperserver.food.fixture;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.domain.StorageMethod;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodCursorFinder;
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
    public static final String MEMBER_ID = "memberId";


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

    public static FoodCursorFinder createFirstPageFinder() {
        return new FoodCursorFinder(
                MEMBER_ID,
                null,
                null,
                null,
                2
        );
    }

    public static Food createFood(Long id) {
        return Food.builder()
                .id(id)
                .name(NAME)
                .imageUrl(IMAGE_URL)
                .storageMethod(STORAGE_METHOD)
                .expiryDate(EXPIRY_DATE)
                .memo(MEMO)
                .selectedCategoryCount(1)
                .memberId(MEMBER_ID)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static FoodEntity createFoodEntity(Long id) {
        FoodEntity foodEntity = FoodEntity.from(createFood(id));
        ReflectionTestUtils.setField(foodEntity, "id", id);
        return foodEntity;
    }

}
