package com.foodkeeper.foodkeeperserver.food.business;

import com.foodkeeper.foodkeeperserver.bookmarkedfood.dataaccess.entity.BookmarkedFoodEntity;
import com.foodkeeper.foodkeeperserver.bookmarkedfood.dataaccess.repository.BookmarkedFoodRepository;
import com.foodkeeper.foodkeeperserver.bookmarkedfood.implement.FoodBookmarker;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodCategoryRepository;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodRepository;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.SelectedFoodCategoryRepository;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodRegister;
import com.foodkeeper.foodkeeperserver.food.fixture.CategoryFixture;
import com.foodkeeper.foodkeeperserver.food.fixture.FoodFixture;
import com.foodkeeper.foodkeeperserver.food.implement.FoodCategoryManager;
import com.foodkeeper.foodkeeperserver.food.implement.FoodManager;
import com.foodkeeper.foodkeeperserver.food.implement.ImageManager;
import com.foodkeeper.foodkeeperserver.food.implement.SelectedFoodCategoryManager;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FoodServiceTest {

    @InjectMocks FoodService foodService;
    @Mock ImageManager imageManager;
    @Mock FoodRepository foodRepository;
    @Mock FoodCategoryRepository foodCategoryRepository;
    @Mock SelectedFoodCategoryRepository selectedFoodCategoryRepository;
    @Mock BookmarkedFoodRepository bookmarkedFoodRepository;

    @BeforeEach
    void setUp() {
        FoodManager foodManager = new FoodManager(foodRepository);
        FoodCategoryManager foodCategoryManager = new FoodCategoryManager(foodCategoryRepository);
        SelectedFoodCategoryManager selectedFoodCategoryManager =
                new SelectedFoodCategoryManager(selectedFoodCategoryRepository);
        FoodBookmarker foodBookmarker = new FoodBookmarker(bookmarkedFoodRepository);

        foodService = new FoodService(
                imageManager,
                foodManager,
                foodCategoryManager,
                selectedFoodCategoryManager,
                foodBookmarker
        );
    }

    @Test
    @DisplayName("식제품 추가 기능 구현 성공")
    void registerFood_SUCCESS() {
        // given
        String memberId = "memberId";
        MultipartFile mockFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "data".getBytes());
        List<Long> categoryIds = List.of(1L, 2L);
        FoodRegister dto = FoodFixture.createRegisterDto(categoryIds);

        FoodEntity mockFoodEntity = FoodFixture.createFoodEntity();
        List<FoodCategoryEntity> mockCategories = CategoryFixture.createCategoryEntity(categoryIds);

        given(imageManager.fileUpload(any())).willReturn(CompletableFuture.completedFuture("파일 경로"));
        given(foodCategoryRepository.findAllById(categoryIds)).willReturn(mockCategories);
        given(foodRepository.save(any(FoodEntity.class))).willReturn(mockFoodEntity);

        // when
        foodService.registerFood(dto, mockFile, memberId);

        // then
        ArgumentCaptor<FoodEntity> foodCaptor = ArgumentCaptor.forClass(FoodEntity.class);
        verify(foodRepository).save(foodCaptor.capture());
        FoodEntity savedFood = foodCaptor.getValue();

        assertThat(savedFood.getName()).isEqualTo(dto.name());
        assertThat(savedFood.getImageUrl()).isEqualTo("파일 경로");

    }

    @Test
    @DisplayName("식재료를 즐겨찾기에 추가한다.")
    void bookmarkFood() {
        // given
        long bookmarkedFoodId = 2L;
        Food food = FoodFixture.createFood();
        FoodEntity foodEntity = FoodEntity.from(food);
        BookmarkedFoodEntity bookmarkedFoodEntity = mock(BookmarkedFoodEntity.class);
        given(bookmarkedFoodEntity.getId()).willReturn(bookmarkedFoodId);
        given(foodRepository.findById(eq(1L))).willReturn(Optional.of(foodEntity));
        given(bookmarkedFoodRepository.save(any(BookmarkedFoodEntity.class))).willReturn(bookmarkedFoodEntity);

        // when
        Long savedFoodId = foodService.bookmarkFood(1L, "memberKey");

        // then
        assertThat(savedFoodId).isEqualTo(bookmarkedFoodId);
    }

    @Test
    @DisplayName("즐겨찾기에 추가할 식재료 데이터가 없으면 AppException이 발생한다.")
    void throwAppExceptionIfOriginFoodNotExists() {
        // given
        given(foodRepository.findById(eq(1L))).willReturn(Optional.empty());

        // then
        Assertions.assertThatThrownBy(() -> foodService.bookmarkFood(1L, "memberKey"))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.NOT_FOUND_DATA);
    }
}
