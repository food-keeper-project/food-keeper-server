package com.foodkeeper.foodkeeperserver.bookmarkedfood.implement;

import com.foodkeeper.foodkeeperserver.bookmarkedfood.dataaccess.entity.BookmarkedFoodEntity;
import com.foodkeeper.foodkeeperserver.bookmarkedfood.dataaccess.repository.BookmarkedFoodRepository;
import com.foodkeeper.foodkeeperserver.bookmarkedfood.domain.BookmarkedFood;
import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookmarkedFoodFinder {

    private final BookmarkedFoodRepository bookmarkedFoodRepository;

    public SliceObject<BookmarkedFood> find(Cursorable<Long> cursorable, String memberKey) {
        return bookmarkedFoodRepository.findBookmarkedFoods(cursorable, memberKey)
                .map(BookmarkedFoodEntity::toBookmarkedFood);
    }
}
