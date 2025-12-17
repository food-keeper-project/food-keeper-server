package com.foodkeeper.foodkeeperserver.bookmarkedfood.implement;

import com.foodkeeper.foodkeeperserver.bookmarkedfood.dataaccess.entity.BookmarkedFoodEntity;
import com.foodkeeper.foodkeeperserver.bookmarkedfood.dataaccess.repository.BookmarkedFoodRepository;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class FoodBookmarkerTest {

    @Mock BookmarkedFoodRepository bookmarkedFoodRepository;
    @InjectMocks FoodBookmarker foodBookmarker;

    @Test
    @DisplayName("식재료를 즐겨찾기에 추가한다.")
    void bookmarkFood() {
        // given
        long bookmarkedFoodId = 2L;
        Food food = mock(Food.class);
        BookmarkedFoodEntity bookmarkedFoodEntity = mock(BookmarkedFoodEntity.class);
        given(bookmarkedFoodEntity.getId()).willReturn(bookmarkedFoodId);
        given(bookmarkedFoodRepository.save(any(BookmarkedFoodEntity.class))).willReturn(bookmarkedFoodEntity);

        // when
        Long savedFoodId = foodBookmarker.bookmark(food, "memberKey");

        // then
        assertThat(savedFoodId).isEqualTo(bookmarkedFoodId);
    }
}