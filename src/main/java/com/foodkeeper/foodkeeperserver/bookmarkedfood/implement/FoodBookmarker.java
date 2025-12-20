package com.foodkeeper.foodkeeperserver.bookmarkedfood.implement;

import com.foodkeeper.foodkeeperserver.bookmarkedfood.dataaccess.entity.BookmarkedFoodEntity;
import com.foodkeeper.foodkeeperserver.bookmarkedfood.dataaccess.repository.BookmarkedFoodRepository;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FoodBookmarker {
    private final BookmarkedFoodRepository bookmarkedFoodRepository;

    @Transactional
    public Long bookmark(Food food, String memberKey) {
        return bookmarkedFoodRepository.save(BookmarkedFoodEntity.from(food, memberKey)).getId();
    }
}