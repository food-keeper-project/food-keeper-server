package com.foodkeeper.foodkeeperserver.bookmarkedfood.business;

import com.foodkeeper.foodkeeperserver.bookmarkedfood.domain.BookmarkedFood;
import com.foodkeeper.foodkeeperserver.bookmarkedfood.implement.BookmarkedFoodFinder;
import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkedFoodService {

    private final BookmarkedFoodFinder bookmarkedFoodFinder;

    public SliceObject<BookmarkedFood> findBookmarkedFoods(Cursorable<Long> cursorable, String memberKey) {
        return bookmarkedFoodFinder.find(cursorable, memberKey);
    }
}
