package com.foodkeeper.foodkeeperserver.food.business;

import com.foodkeeper.foodkeeperserver.bookmarkedfood.dataaccess.entity.BookmarkedFoodEntity;
import com.foodkeeper.foodkeeperserver.bookmarkedfood.dataaccess.repository.BookmarkedFoodRepository;
import com.foodkeeper.foodkeeperserver.bookmarkedfood.implement.FoodBookmarker;
import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.SelectedFoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodCategoryRepository;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodRepository;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.SelectedFoodCategoryRepository;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.domain.RecipeFood;
import com.foodkeeper.foodkeeperserver.food.domain.RegisteredFood;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodRegister;
import com.foodkeeper.foodkeeperserver.food.fixture.CategoryFixture;
import com.foodkeeper.foodkeeperserver.food.fixture.FoodFixture;
import com.foodkeeper.foodkeeperserver.food.fixture.SelectedFoodCategoryFixture;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FoodServiceTest {

    @InjectMocks
    FoodService foodService;
    @Mock
    ImageManager imageManager;
    @Mock
    FoodRepository foodRepository;
    @Mock
    FoodCategoryRepository foodCategoryRepository;
    @Mock
    SelectedFoodCategoryRepository selectedFoodCategoryRepository;
    @Mock
    BookmarkedFoodRepository bookmarkedFoodRepository;

    @BeforeEach
    void setUp() {
        FoodManager foodManager = new FoodManager(foodRepository);
        FoodCategoryManager foodCategoryManager = new FoodCategoryManager(foodCategoryRepository);
        SelectedFoodCategoryManager selectedFoodCategoryManager = new SelectedFoodCategoryManager(selectedFoodCategoryRepository);
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
    void registerFood_SUCCESS() throws Exception {
        // given
        String memberKey = "memberKey";
        MultipartFile mockFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "data".getBytes());
        List<Long> categoryIds = List.of(1L, 2L);
        FoodRegister dto = FoodFixture.createRegisterDto(categoryIds);

        FoodEntity mockFoodEntity = FoodFixture.createFoodEntity(1L);
        List<FoodCategoryEntity> mockCategories = CategoryFixture.createCategoryEntity(categoryIds);

        given(imageManager.fileUpload(any())).willReturn(CompletableFuture.completedFuture("파일 경로"));
        given(foodCategoryRepository.findAllById(categoryIds)).willReturn(mockCategories);
        given(foodRepository.save(any(FoodEntity.class))).willReturn(mockFoodEntity);

        // when
        foodService.registerFood(dto, mockFile, memberKey);
        // then
        ArgumentCaptor<FoodEntity> foodCaptor = ArgumentCaptor.forClass(FoodEntity.class);
        verify(foodRepository).save(foodCaptor.capture());
        FoodEntity savedFood = foodCaptor.getValue();

        assertThat(savedFood.getName()).isEqualTo(dto.name());
        assertThat(savedFood.getImageUrl()).isEqualTo("파일 경로");

    }

    @Test
    @DisplayName("카테고리가 3개 초과하면 에러 발생")
    void validateCategorySize_FAIL() throws Exception {
        //given
        List<Long> categoryIds = List.of(1L, 2L, 3L, 4L);
        MockMultipartFile mockImage = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test data".getBytes());
        FoodRegister registerDto = FoodFixture.createRegisterDto(categoryIds);
        given(imageManager.fileUpload(any()))
                .willReturn(CompletableFuture.completedFuture("https://dummy-url.com/image.jpg"));
        //when + then
        assertThatThrownBy(() -> foodService.registerFood(registerDto, mockImage, "memberKey"))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.DEFAULT_ERROR);
    }

    @Test
    @DisplayName("커서 조회 시 limit 보다 많으면 hasNext 는 true 이고, 초과되면 하나는 제거되고 카테고리 매핑")
    void getFoodList_hasNext_TRUE() throws Exception {
        //given
        Long categoryId = 1L;
        String memberKey = FoodFixture.MEMBER_KEY;
        LocalDateTime lastCreatedAt = LocalDateTime.now();
        Cursorable<LocalDateTime> cursorable = new Cursorable<>(lastCreatedAt, 2);


        List<FoodEntity> foodEntities = new ArrayList<>();
        foodEntities.add(FoodFixture.createFoodEntity(1L));
        foodEntities.add(FoodFixture.createFoodEntity(2L));
        foodEntities.add(FoodFixture.createFoodEntity(3L));


        List<SelectedFoodCategoryEntity> selectedFoodCategories = List.of(
                SelectedFoodCategoryFixture.createSelectedCategoryEntity(1L, 1L),
                SelectedFoodCategoryFixture.createSelectedCategoryEntity(2L, 2L)
        );
        given(foodRepository.findFoodCursorList(cursorable, categoryId, memberKey)).willReturn(foodEntities);
        given(selectedFoodCategoryRepository.findByFoodIdIn(anyList())).willReturn(selectedFoodCategories);
        //when
        SliceObject<RegisteredFood> result = foodService.getFoodList(cursorable, categoryId, memberKey);
        //then
        assertThat(result.hasNext()).isTrue();
        assertThat(result.content()).hasSize(2);
        assertThat(result.content().getFirst().categoryIds().getFirst()).isEqualTo(1L);
    }

    @Test
    @DisplayName("식재료 단일 조회 시 카테고리와 매핑된 상태로 결과 반환")
    void getFood_SUCCESS() throws Exception {
        //given
        FoodEntity food = FoodFixture.createFoodEntity(1L);
        List<SelectedFoodCategoryEntity> selectedFoodCategories = List.of(
                SelectedFoodCategoryFixture.createSelectedCategoryEntity(1L, 1L),
                SelectedFoodCategoryFixture.createSelectedCategoryEntity(1L, 2L)
        );
        given(foodRepository.findByIdAndMemberKey(1L, FoodFixture.MEMBER_KEY)).willReturn(Optional.of(food));
        given(selectedFoodCategoryRepository.findByFoodId(1L)).willReturn(selectedFoodCategories);
        //when
        RegisteredFood result = foodService.getFood(1L, FoodFixture.MEMBER_KEY);
        //then
        assertThat(result.categoryIds()).hasSize(2).containsExactly(1L, 2L);

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

    @DisplayName("foodId 리스트와 memberKey 으로 foodName을 List<String>로 결과 반환")
    void getFoodNames_SUCCESS() throws Exception {
        //given
        List<Long> ids = List.of(1L, 2L);
        String memberKey = FoodFixture.MEMBER_KEY;
        String foodName = FoodFixture.NAME;
        FoodEntity food1 = FoodFixture.createFoodEntity(ids.get(0));
        FoodEntity food2 = FoodFixture.createFoodEntity(ids.get(1));
        given(foodRepository.findAllByMemberKey(memberKey)).willReturn(List.of(food1, food2));
        //when
        List<RecipeFood> foods = foodService.getAllByMemberKey(memberKey);
        //then
        assertThat(foods).hasSize(2);
        assertThat(foods.getFirst().name()).isEqualTo(foodName);
    }

    @Test
    @DisplayName("임박한 재료 리스트를 조회했을 때 foodName, remainDays 반환")
    void getImminentFood_SUCCESS() throws Exception {
        //given
        String memberKey = FoodFixture.MEMBER_KEY;
        FoodEntity food1 = FoodFixture.createFoodEntity(1L);
        FoodEntity food2 = FoodFixture.createFoodEntity(2L);

        given(foodRepository.findImminentFoods(memberKey)).willReturn(List.of(food1, food2));
        //when
        List<RecipeFood> results = foodService.getImminentFoods(memberKey);
        //then
        assertThat(results).hasSize(2);
        assertThat(results.getFirst().name()).isEqualTo(food1.getName());
        assertThat(results.getFirst().remainDay()).isEqualTo(1L); // FoodFixture.EXPIRY_DATE = 내일
    }


}
