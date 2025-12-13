package com.foodkeeper.foodkeeperserver.bookmarkedfood.implement;

import com.foodkeeper.foodkeeperserver.bookmarkedfood.dataaccess.entity.BookmarkedFoodEntity;
import com.foodkeeper.foodkeeperserver.bookmarkedfood.dataaccess.repository.BookmarkedFoodRepository;
import com.foodkeeper.foodkeeperserver.bookmarkedfood.domain.Food;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FoodBookmarker {
    private final BookmarkedFoodRepository bookmarkedFoodRepository;

    @Transactional
    public Long bookmark(Food food, String memberKey) {
        BookmarkedFoodEntity bookmarkedFoodEntity = BookmarkedFoodEntity.builder()
                .name(food.getName())
                .imageUrl(food.getImageUrl())
                .storageMethod(food.getStorageMethod())
                .memberKey(memberKey)
                .build();

        return bookmarkedFoodRepository.save(bookmarkedFoodEntity).getId();
    }
}