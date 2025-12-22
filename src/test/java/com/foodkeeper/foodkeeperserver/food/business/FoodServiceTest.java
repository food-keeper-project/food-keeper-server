package com.foodkeeper.foodkeeperserver.food.business;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.SelectedFoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodCategoryRepository;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodRepository;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.SelectedFoodCategoryRepository;
import com.foodkeeper.foodkeeperserver.food.domain.RegisteredFood;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodRegister;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodsFinder;
import com.foodkeeper.foodkeeperserver.food.domain.response.FoodCursorResult;
import com.foodkeeper.foodkeeperserver.food.domain.response.RecipeFood;
import com.foodkeeper.foodkeeperserver.food.fixture.CategoryFixture;
import com.foodkeeper.foodkeeperserver.food.fixture.FoodFixture;
import com.foodkeeper.foodkeeperserver.food.fixture.SelectedFoodCategoryFixture;
import com.foodkeeper.foodkeeperserver.food.implement.FoodCategoryManager;
import com.foodkeeper.foodkeeperserver.food.implement.FoodManager;
import com.foodkeeper.foodkeeperserver.food.implement.ImageManager;
import com.foodkeeper.foodkeeperserver.food.implement.SelectedFoodCategoryManager;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
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

    @BeforeEach
    void setUp() {
        FoodManager foodManager = new FoodManager(foodRepository);
        FoodCategoryManager foodCategoryManager = new FoodCategoryManager(foodCategoryRepository);
        SelectedFoodCategoryManager selectedFoodCategoryManager = new SelectedFoodCategoryManager(selectedFoodCategoryRepository);

        foodService = new FoodService(
                imageManager,
                foodManager,
                foodCategoryManager,
                selectedFoodCategoryManager
        );
    }

    @Test
    @DisplayName("식제품 추가 기능 구현 성공")
    void registerFood_SUCCESS() throws Exception {
        // given
        String memberId = FoodFixture.MEMBER_ID;
        MultipartFile mockFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "data".getBytes());
        List<Long> categoryIds = List.of(1L, 2L);
        FoodRegister dto = FoodFixture.createRegisterDto(categoryIds);

        FoodEntity mockFoodEntity = FoodFixture.createFoodEntity(1L);
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
    @DisplayName("카테고리가 3개 초과하면 에러 발생")
    void validateCategorySize_FAIL() throws Exception {
        //given
        List<Long> categoryIds = List.of(1L, 2L, 3L, 4L);
        MockMultipartFile mockImage = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test data".getBytes());
        FoodRegister registerDto = FoodFixture.createRegisterDto(categoryIds);
        given(imageManager.fileUpload(any()))
                .willReturn(CompletableFuture.completedFuture("https://dummy-url.com/image.jpg"));
        //when + then
        assertThatThrownBy(() -> foodService.registerFood(registerDto, mockImage, "memberId"))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.DEFAULT_ERROR);
    }

    @Test
    @DisplayName("커서 조회 시 limit 보다 많으면 hasNext 는 true 이고, 초과되면 하나는 제거되고 카테고리 매핑")
    void getFoodList_hasNext_TRUE() throws Exception {
        //given
        FoodsFinder finder = FoodFixture.createFirstPageFinder();
        List<FoodEntity> foodEntities = new ArrayList<>();
        foodEntities.add(FoodFixture.createFoodEntity(1L));
        foodEntities.add(FoodFixture.createFoodEntity(2L));
        foodEntities.add(FoodFixture.createFoodEntity(3L));


        List<SelectedFoodCategoryEntity> selectedFoodCategories = List.of(
                SelectedFoodCategoryFixture.createSelectedCategoryEntity(1L, 1L),
                SelectedFoodCategoryFixture.createSelectedCategoryEntity(2L, 2L)
        );
        given(foodRepository.findFoodCursorList(finder)).willReturn(foodEntities);
        given(selectedFoodCategoryRepository.findByFoodIdIn(List.of(1L, 2L))).willReturn(selectedFoodCategories);
        //when
        FoodCursorResult result = foodService.getFoodList(finder);
        //then
        assertThat(result.hasNext()).isTrue();
        assertThat(result.foods()).hasSize(2);
        assertThat(result.foods().getFirst().categoryIds().getFirst()).isEqualTo(1L);
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

        given(foodRepository.findByIdAndMemberId(1L, FoodFixture.MEMBER_ID)).willReturn(Optional.of(food));
        given(selectedFoodCategoryRepository.findByFoodId(1L)).willReturn(selectedFoodCategories);
        //when
        RegisteredFood result = foodService.getFood(1L, FoodFixture.MEMBER_ID);
        //then
        assertThat(result.categoryIds()).hasSize(2).containsExactly(1L, 2L);

    }

    @Test
    @DisplayName("foodId 리스트와 memberId 으로 foodName을 List<String>로 결과 반환")
    void getFoodNames_SUCCESS() throws Exception {
        //given
        List<Long> ids = List.of(1L, 2L);
        String memberId = FoodFixture.MEMBER_ID;
        String foodName = FoodFixture.NAME;
        FoodEntity food1 = FoodFixture.createFoodEntity(ids.get(0));
        FoodEntity food2 = FoodFixture.createFoodEntity(ids.get(1));
        given(foodRepository.findAllByMemberId(memberId)).willReturn(List.of(food1, food2));
        //when
        List<RecipeFood> foods = foodService.getAllByMemberId(memberId);
        //then
        assertThat(foods).hasSize(2);
        assertThat(foods.getFirst().name()).isEqualTo(foodName);
    }

    @Test
    @DisplayName("임박한 재료 리스트를 조회했을 때 foodName, remainDays 반환")
    void getImminentFood_SUCCESS() throws Exception {
        //given
        String memberId = FoodFixture.MEMBER_ID;
        FoodEntity food1 = FoodFixture.createFoodEntity(1L);
        FoodEntity food2 = FoodFixture.createFoodEntity(2L);

        given(foodRepository.findImminentFoods(memberId)).willReturn(List.of(food1, food2));
        //when
        List<RecipeFood> results = foodService.getImminentFoods(memberId);
        //then
        assertThat(results).hasSize(2);
        assertThat(results.getFirst().name()).isEqualTo(food1.getName());
        assertThat(results.getFirst().remainDay()).isEqualTo(1L); // FoodFixture.EXPIRY_DATE = 내일
    }

    @Test
    @DisplayName("식재료 삭제 시 식재료와 매핑된 카테고리, 저장된 사진 삭제")
    void removeFood_SUCCESS() throws Exception {
        //given
        Long foodId = 1L;
        String memberId = FoodFixture.MEMBER_ID;

        FoodEntity food = FoodFixture.createFoodEntity(foodId);

        given(foodRepository.findByIdAndMemberId(foodId, memberId)).willReturn((Optional.of(food)));
        willDoNothing().given(selectedFoodCategoryRepository).deleteAllByFoodId(foodId);

        //when
        Long resultId = foodService.removeFood(foodId, memberId);
        //then
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(selectedFoodCategoryRepository).deleteAllByFoodId(captor.capture());

        assertThat(captor.getValue()).isEqualTo(foodId);
        assertThat(food.isDeleted()).isTrue();
        assertThat(food.getId()).isEqualTo(resultId);
    }
}
