package com.foodkeeper.foodkeeperserver.bookmarkedfood.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.bookmarkedfood.dataaccess.entity.BookmarkedFoodEntity;
import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;

public interface BookmarkedFoodRepositoryCustom {

    SliceObject<BookmarkedFoodEntity> findBookmarkedFoods(Cursorable<Long> cursorable, String memberKey);
}
