package com.foodkeeper.foodkeeperserver.bookmarkedfood.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.bookmarkedfood.dataaccess.entity.BookmarkedFoodEntity;
import com.foodkeeper.foodkeeperserver.bookmarkedfood.dataaccess.repository.custom.BookmarkedFoodRepositoryCustom;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

@NullMarked
public interface BookmarkedFoodRepository extends JpaRepository<BookmarkedFoodEntity, Long>,
        BookmarkedFoodRepositoryCustom {

}
