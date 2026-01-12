package com.foodkeeper.foodkeeperserver.food.business;

import com.foodkeeper.foodkeeperserver.ai.AiProcessor;
import com.foodkeeper.foodkeeperserver.ai.domain.*;
import com.foodkeeper.foodkeeperserver.bookmarkedfood.dataaccess.entity.BookmarkedFoodEntity;
import com.foodkeeper.foodkeeperserver.bookmarkedfood.dataaccess.repository.BookmarkedFoodRepository;
import com.foodkeeper.foodkeeperserver.bookmarkedfood.implement.FoodBookmarker;
import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.enums.EntityStatus;
import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;
import com.foodkeeper.foodkeeperserver.common.handler.TransactionHandler;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.SelectedFoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodCategoryRepository;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodRepository;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.SelectedFoodCategoryRepository;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.domain.ScannedFood;
import com.foodkeeper.foodkeeperserver.food.domain.RegisteredFood;
import com.foodkeeper.foodkeeperserver.food.domain.StorageMethod;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodRegister;
import com.foodkeeper.foodkeeperserver.food.fixture.CategoryFixture;
import com.foodkeeper.foodkeeperserver.food.fixture.FoodFixture;
import com.foodkeeper.foodkeeperserver.food.fixture.SelectedFoodCategoryFixture;
import com.foodkeeper.foodkeeperserver.food.implement.*;
import com.foodkeeper.foodkeeperserver.ai.implement.AiFoodScanner;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.ClovaClient;
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
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
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
    @Mock
    TransactionHandler transactionHandler;
    @Mock
    ClovaClient clovaClient;


    @BeforeEach
    void setUp() {
        FoodManager foodManager = new FoodManager(foodRepository, new SelectedFoodCategoryManager(selectedFoodCategoryRepository));
        CategoryManager categoryManager = new CategoryManager(foodCategoryRepository);
        FoodCategoryReader foodCategoryReader = new FoodCategoryReader(foodCategoryRepository, selectedFoodCategoryRepository);
        FoodReader foodReader = new FoodReader(foodRepository, foodCategoryReader);
        SelectedFoodCategoryManager selectedFoodCategoryManager = new SelectedFoodCategoryManager(selectedFoodCategoryRepository);
        FoodBookmarker foodBookmarker = new FoodBookmarker(bookmarkedFoodRepository);
        AiProcessor processor = new AiProcessor(clovaClient, new ObjectMapper());
        AiFoodScanner foodScanner = new AiFoodScanner(processor);

        foodService = new FoodService(
                foodReader,
                imageManager,
                foodManager,
                categoryManager,
                selectedFoodCategoryManager,
                foodBookmarker,
                transactionHandler,
                foodScanner
        );
    }

    @Test
    @DisplayName("식제품 추가 기능 구현 성공")
    void registerFood_SUCCESS() {
        // given
        String memberKey = "memberKey";
        MultipartFile mockFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "data".getBytes());
        List<Long> categoryIds = List.of(1L, 2L);
        FoodRegister dto = FoodFixture.createRegister(categoryIds);

        FoodEntity mockFoodEntity = FoodFixture.createFoodEntity(1L);
        List<FoodCategoryEntity> mockCategories = CategoryFixture.createCategoryEntity(categoryIds);

        given(imageManager.fileUpload(any())).willReturn(Optional.of("파일 경로"));
        given(foodCategoryRepository.findAllById(categoryIds)).willReturn(mockCategories);
        given(foodRepository.save(any(FoodEntity.class))).willReturn(mockFoodEntity);
        willDoNothing().given(transactionHandler).runOnRollback(any());

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
    @DisplayName("커서 조회 시 limit 보다 많으면 hasNext 는 true 이고, 초과되면 하나는 제거되고 카테고리 매핑")
    void getFoodList_hasNext_TRUE() {
        //given
        Long categoryId = 1L;
        String memberKey = FoodFixture.MEMBER_KEY;
        Cursorable<Long> cursorable = new Cursorable<>(1L, 2);

        List<FoodEntity> foodEntities = List.of(FoodFixture.createFoodEntity(1L),
                FoodFixture.createFoodEntity(2L));
        SliceObject<FoodEntity> foodSlice = new SliceObject<>(foodEntities, cursorable, true);

        List<FoodCategoryEntity> foodCategories = CategoryFixture.createCategoryEntity(List.of(1L, 2L));

        List<SelectedFoodCategoryEntity> selectedFoodCategories = List.of(
                SelectedFoodCategoryFixture.createSelectedCategoryEntity(1L, 1L),
                SelectedFoodCategoryFixture.createSelectedCategoryEntity(2L, 2L)
        );

        given(foodRepository.findFoods(cursorable, categoryId, memberKey)).willReturn(foodSlice);
        given(selectedFoodCategoryRepository.findByFoodIdIn(anyList())).willReturn(selectedFoodCategories);
        given(foodCategoryRepository.findAllByIdIn(List.of(1L, 2L))).willReturn(foodCategories);

        //when
        SliceObject<RegisteredFood> result = foodService.findFoodList(cursorable, categoryId, memberKey);

        //then
        assertThat(result.hasNext()).isTrue();
        assertThat(result.content()).hasSize(2);
        assertThat(result.content().getFirst().categories().getFirst().name()).isEqualTo("유제품");
    }

    @Test
    @DisplayName("식재료 단일 조회 시 카테고리와 매핑된 상태로 결과 반환")
    void getFood_SUCCESS() {
        //given
        FoodEntity food = FoodFixture.createFoodEntity(1L);
        List<SelectedFoodCategoryEntity> selectedFoodCategories = List.of(
                SelectedFoodCategoryFixture.createSelectedCategoryEntity(1L, 1L),
                SelectedFoodCategoryFixture.createSelectedCategoryEntity(1L, 2L)
        );
        List<FoodCategoryEntity> foodCategories = CategoryFixture.createCategoryEntity(List.of(1L, 2L));

        given(foodRepository.findByIdAndMemberKey(1L, FoodFixture.MEMBER_KEY)).willReturn(Optional.of(food));
        given(selectedFoodCategoryRepository.findByFoodId(1L)).willReturn(selectedFoodCategories);
        given(foodCategoryRepository.findAllByIdIn(List.of(1L, 2L))).willReturn(foodCategories);

        //when
        RegisteredFood result = foodService.findFood(1L, FoodFixture.MEMBER_KEY);

        //then
        assertThat(result.categories()).hasSize(2);
    }

    @Test
    @DisplayName("식재료를 즐겨찾기에 추가한다.")
    void bookmarkFood() {
        // given
        long bookmarkedFoodId = 2L;
        Food food = FoodFixture.createFood(1L);
        FoodRegister foodRegister = new FoodRegister(food.name(), List.of(), food.storageMethod(), food.expiryDate(),
                food.expiryAlarmDays(), food.memo());
        FoodEntity foodEntity = FoodEntity.from(foodRegister, food.imageUrl(), food.memberKey());
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

    @Test
    @DisplayName("foodId 리스트와 memberKey 으로 foodName을 List<String>로 결과 반환")
    void getFoodNames_SUCCESS() {
        //given
        List<Long> ids = List.of(1L, 2L);
        String memberKey = FoodFixture.MEMBER_KEY;
        String foodName = FoodFixture.NAME;
        FoodEntity food1 = FoodFixture.createFoodEntity(ids.get(0));
        FoodEntity food2 = FoodFixture.createFoodEntity(ids.get(1));
        given(foodRepository.findAllByMemberKey(memberKey)).willReturn(List.of(food1, food2));
        //when
        List<RegisteredFood> foods = foodService.findAllFoods(memberKey);
        //then
        assertThat(foods).hasSize(2);
        assertThat(foods.getFirst().name()).isEqualTo(foodName);
    }

    @Test
    @DisplayName("임박한 재료 리스트를 조회했을 때 foodName, remainDays 반환")
    void getImminentFood_SUCCESS() {
        //given
        String memberKey = "memberKey";
        List<Long> foodIds = List.of(1L, 2L);

        List<FoodEntity> foodEntities = List.of(FoodFixture.createFoodEntity(1L), FoodFixture.createFoodEntity(2L));

        given(foodRepository.findImminentFoods(any(LocalDate.class), eq(memberKey)))
                .willReturn(foodEntities);

        SelectedFoodCategoryEntity selectedEntity = SelectedFoodCategoryFixture.createSelectedCategoryEntity(1L, 1L);
        given(selectedFoodCategoryRepository.findByFoodIdIn(foodIds))
                .willReturn(List.of(selectedEntity));

        FoodCategoryEntity categoryEntity = CategoryFixture.createCategoryEntity(1L);
        given(foodCategoryRepository.findAllByIdIn(List.of(1L)))
                .willReturn(List.of(categoryEntity));

        //when
        List<RegisteredFood> results = foodService.findImminentFoods(memberKey);
        //then
        assertThat(results.getFirst().name()).isEqualTo("우유");
        assertThat(results.getFirst().categories()).hasSize(1);
        assertThat(results.getFirst().remainDays()).isEqualTo(1L);
    }

    @Test
    @DisplayName("식재료 삭제 시 식재료와 매핑된 카테고리, 저장된 사진 삭제")
    void removeFood_SUCCESS() {
        //given
        Long foodId = 1L;
        String memberKey = FoodFixture.MEMBER_KEY;

        FoodEntity food = FoodFixture.createFoodEntity(foodId);

        given(foodRepository.findByIdAndMemberKey(foodId, memberKey)).willReturn((Optional.of(food)));
        willDoNothing().given(selectedFoodCategoryRepository).deleteAllByFoodId(foodId);

        //when
        foodService.removeFood(foodId, memberKey);
        //then
        assertThat(food.getStatus()).isEqualTo(EntityStatus.DELETED);
        assertThat(food.getStatus()).isEqualTo(EntityStatus.DELETED);
    }

    @Test
    @DisplayName("식재료 수정시 null 로 들어오면 기존의 데이터로 저장")
    void updateFood_SUCCESS() {
        //given
        Long foodId = 1L;
        MultipartFile mockFile = mock(MultipartFile.class);
        FoodRegister foodRegister = new FoodRegister(
                "음료", List.of(1L, 2L), StorageMethod.FROZEN, null, null, null);
        FoodEntity foodEntity = FoodFixture.createFoodEntity(foodId);

        given(foodRepository.findById(foodId)).willReturn(Optional.ofNullable(foodEntity));
        assertNotNull(foodEntity);

        given(imageManager.fileUpload(any())).willReturn(Optional.of("파일 경로"));
        willDoNothing().given(imageManager).deleteFile(any());
        willDoNothing().given(selectedFoodCategoryRepository).deleteAllByFoodId(foodId);
        given(selectedFoodCategoryRepository.saveAll(any())).willReturn(List.of());

        //when
        foodService.updateFood(foodId, foodRegister, mockFile, FoodFixture.MEMBER_KEY);

        //then
        assertThat(foodEntity.getName()).isEqualTo(foodRegister.name());
        assertThat(foodEntity.getMemo()).isEqualTo(FoodFixture.MEMO);
    }

    @Test
    @DisplayName("텍스트를 받아서 AI를 이용해 필요한 데이터만 추출 성공")
    void parseText_SUCCESS() {
        //given
        String clovaContent = """
            {
                "name" : "신라면",
                "storageMethod" : "냉장",
                "expiryDate" : "2025-05-05"
            }    
            """;
        ClovaMessage clovaMessage = new ClovaMessage(AiType.SYSTEM, clovaContent);
        ClovaResponse clovaResponse = new ClovaResponse(
                new ClovaResponseStatus("code", "message"),
                new ClovaResult(clovaMessage));
        given(clovaClient.getAiResponse(anyString(), any())).willReturn(clovaResponse);

        //when
        ScannedFood scannedFood = foodService.scanFoodByOcr("test-ocr-text");
        //then
        assertThat(scannedFood.name()).isEqualTo("신라면");
        assertThat(scannedFood.storageMethod()).isEqualTo(StorageMethod.REFRIGERATED);
        assertThat(scannedFood.expiryDate()).isEqualTo("2025-05-05");
    }

}
