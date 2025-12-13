package com.foodkeeper.foodkeeperserver.bookmarkedfood.business;

import com.foodkeeper.foodkeeperserver.bookmarkedfood.domain.Food;
import com.foodkeeper.foodkeeperserver.bookmarkedfood.implement.FoodBookmarker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkedFoodService {
    private final FoodBookmarker foodBookmarker;

    public Long bookmarkFood(Long foodId, String memberKey) {
        // TODO: find to Food by FoodFinder

        return foodBookmarker.bookmark(new Food(), memberKey);
    }
}
