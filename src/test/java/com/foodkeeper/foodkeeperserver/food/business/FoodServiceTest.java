package com.foodkeeper.foodkeeperserver.food.business;

import com.foodkeeper.foodkeeperserver.food.implement.ImageManager;
import com.foodkeeper.foodkeeperserver.food.dto.request.FoodRegisterRequest;
import com.foodkeeper.foodkeeperserver.food.entity.Food;
import com.foodkeeper.foodkeeperserver.food.entity.FoodCategory;
import com.foodkeeper.foodkeeperserver.food.fixture.CategoryFixture;
import com.foodkeeper.foodkeeperserver.food.fixture.FoodFixture;
import com.foodkeeper.foodkeeperserver.food.implement.FoodCategoryFinder;
import com.foodkeeper.foodkeeperserver.food.implement.FoodCreator;
import com.foodkeeper.foodkeeperserver.food.implement.SelectedFoodCategoryCreator;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FoodServiceTest {

    @InjectMocks
    private FoodService foodService;

    @Mock
    private ImageManager imageManager;

    @Mock
    private FoodCreator foodCreator;

    @Mock
    private FoodCategoryFinder foodCategoryFinder;

    @Mock
    private SelectedFoodCategoryCreator selectedFoodCategoryCreator;

    @Test
    @DisplayName("식제품 추가 기능 구현 성공")
    void registerFood_SUCCESS() throws Exception {
        // given
        String memberId = "user-test-id";
        MultipartFile mockFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "data".getBytes());

        Food mockFood = FoodFixture.createFood(1L);
        List<Long> categoryIds = List.of(1L, 2L);

        FoodRegisterRequest request = FoodFixture.createFoodRegisterRequest(categoryIds);
        List<FoodCategory> mockCategories = CategoryFixture.createCategory(categoryIds);

        given(imageManager.toUrls(any())).willReturn("20251212/uuid.jpg");
        given(foodCategoryFinder.findAll(anyList())).willReturn(mockCategories);
        given(foodCreator.save(any(Food.class))).willReturn(mockFood);

        // when
        Long resultId = foodService.registerFood(request, mockFile, memberId);

        // then
        verify(foodCreator).save(argThat(food ->
                food.getName().equals("우유") &&
                        food.getImageUrl().equals("20251212/uuid.jpg") &&
                        food.getMemberId().equals(memberId)));

        assertThat(resultId).isEqualTo(1L);

        verify(selectedFoodCategoryCreator, times(categoryIds.size())).save(anyLong(), anyLong());
    }

    @Test
    @DisplayName("카테고리가 3개 초과하면 에러 발생")
    void validateCategorySize_FAIL() throws Exception {
        //given
        List<Long> categoryIds = List.of(1L, 2L, 3L, 4L);

        FoodRegisterRequest request = FoodFixture.createFoodRegisterRequest(categoryIds);
        //when + then
        assertThatThrownBy(() -> foodService.registerFood(request, null, "memberId"))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.CATEGORY_SELECT_ERROR);
    }
}
