package com.foodkeeper.foodkeeperserver.bookmarkedfood.implement;

import com.foodkeeper.foodkeeperserver.bookmarkedfood.dataaccess.entity.BookmarkedFoodEntity;
import com.foodkeeper.foodkeeperserver.bookmarkedfood.dataaccess.repository.BookmarkedFoodRepository;
import com.foodkeeper.foodkeeperserver.bookmarkedfood.domain.BookmarkedFood;
import com.foodkeeper.foodkeeperserver.bookmarkedfood.fixture.BookmarkedFoodEntityFixture;
import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BookmarkedFoodFinderTest {

    @Mock
    BookmarkedFoodRepository bookmarkedFoodRepository;
    BookmarkedFoodFinder bookmarkedFoodFinder;

    @BeforeEach
    void setUp() {
        bookmarkedFoodFinder = new BookmarkedFoodFinder(bookmarkedFoodRepository);
    }

    @Test
    @DisplayName("즐겨찾기 된 음식 리스트를 조회한다.")
    void findBookmarkedFoods() {
        // given
        String memberKey = "memberKey";
        Cursorable<Long> cursorable = new Cursorable<>(10L, 10);
        List<BookmarkedFoodEntity> bookmarkedFoodEntities = List.of(BookmarkedFoodEntityFixture.DEFAULT.get(memberKey));
        SliceObject<BookmarkedFoodEntity> slice = new SliceObject<>(bookmarkedFoodEntities, cursorable);
        given(bookmarkedFoodRepository.findBookmarkedFoods(any(), eq(memberKey))).willReturn(slice);

        // when
        SliceObject<BookmarkedFood> bookmarkedFoods = bookmarkedFoodFinder.find(cursorable, memberKey);


        // then
        assertThat(bookmarkedFoods.getContent()).hasSize(1);
        assertThat(bookmarkedFoods.getContent().getFirst().name()).isEqualTo("name");
    }

}