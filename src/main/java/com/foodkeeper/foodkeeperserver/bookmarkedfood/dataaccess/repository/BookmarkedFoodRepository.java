package com.foodkeeper.foodkeeperserver.bookmarkedfood.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.bookmarkedfood.dataaccess.entity.BookmarkedFoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkedFoodRepository extends JpaRepository<BookmarkedFoodEntity, Long> {
}
