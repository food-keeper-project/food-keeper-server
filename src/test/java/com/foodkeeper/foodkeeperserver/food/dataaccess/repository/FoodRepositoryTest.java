package com.foodkeeper.foodkeeperserver.food.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.SelectedFoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.food.domain.StorageMethod;
import com.foodkeeper.foodkeeperserver.support.repository.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class FoodRepositoryTest extends RepositoryTest {

    @Autowired
    FoodRepository foodRepository;
    @Autowired
    SelectedFoodCategoryRepository selectedFoodCategoryRepository;

    record Pair<L, R>(L first, R second) {
    }

    @Test
    @DisplayName("foodCategoryId와 memberKey에 해당하는 Food들을 페이지로 조회한다.")
    void findFoodsByFoodCategoryIdAndMemberKeyWithPagination() {
        // given
        String memberKey = "memberKey";
        long foodCategoryId = 1L;
        List<Pair<String, LocalDate>> foodInfos = List.of(
                new Pair<>("food13", LocalDate.of(2026, 1, 4)),
                new Pair<>("food5", LocalDate.of(2025, 12, 25)),
                new Pair<>("food10", LocalDate.of(2026, 1, 1)),
                new Pair<>("food1", LocalDate.of(2025, 12, 23)),
                new Pair<>("food6", LocalDate.of(2025, 12, 27)),
                new Pair<>("food3", LocalDate.of(2025, 12, 24)),
                new Pair<>("food12", LocalDate.of(2026, 1, 3)),
                new Pair<>("food11", LocalDate.of(2026, 1, 3)),
                new Pair<>("food7", LocalDate.of(2025, 12, 28)),
                new Pair<>("food4", LocalDate.of(2025, 12, 25)),
                new Pair<>("food9", LocalDate.of(2025, 12, 31)),
                new Pair<>("food2", LocalDate.of(2025, 12, 23)),
                new Pair<>("food8", LocalDate.of(2025, 12, 30)),
                new Pair<>("food14", LocalDate.of(2026, 1, 5)),
                new Pair<>("food15", LocalDate.of(2026, 1, 7))
        );
        foodInfos.forEach(foodInfo -> {
            FoodEntity food = em.persist(FoodEntity.builder()
                    .name(foodInfo.first)
                    .imageUrl("https://test.com/image.jpg")
                    .storageMethod(StorageMethod.FROZEN)
                    .expiryDate(foodInfo.second)
                    .expiryAlarmDays(2)
                    .memo("")
                    .selectedCategoryCount(1)
                    .memberKey(memberKey)
                    .build());
            selectedFoodCategoryRepository.save(new SelectedFoodCategoryEntity(food.getId(), foodCategoryId));
        });

        // when
        SliceObject<FoodEntity> foodPage1 = foodRepository.findFoods(new Cursorable<>(null, 7), foodCategoryId, memberKey);
        SliceObject<FoodEntity> foodPage2 = foodRepository.findFoods(new Cursorable<>(foodPage1.content().getLast().getId(), 5), foodCategoryId, memberKey);

        // then
        assertThat(foodPage1.content()).hasSize(7);
        assertThat(foodPage2.content()).hasSize(5);
    }

    @Test
    @DisplayName("memberKey로 해당 멤버의 모든 Food를 조회한다.")
    void findFoodsByMemberKey() {
        // given
        String memberKey = "memberKey";
        List<Pair<String, String>> foodInfos = List.of(
                new Pair<>("food1", memberKey),
                new Pair<>("food2", memberKey),
                new Pair<>("food3", "anotherMemberKey")
        );
        foodInfos.forEach(foodInfo ->
                em.persist(FoodEntity.builder()
                        .name(foodInfo.first)
                        .imageUrl("https://test.com/image.jpg")
                        .storageMethod(StorageMethod.FROZEN)
                        .expiryDate(LocalDate.now())
                        .expiryAlarmDays(2)
                        .memo("")
                        .selectedCategoryCount(1)
                        .memberKey(foodInfo.second)
                        .build()));

        // when
        List<FoodEntity> foods = foodRepository.findAllByMemberKey(memberKey);

        // then
        assertThat(foods).hasSize(2);
        assertThat(foods.getFirst().getMemberKey()).isEqualTo(memberKey);
        assertThat(foods.get(1).getMemberKey()).isEqualTo(memberKey);
    }

    @Test
    @DisplayName("유통기한(expiryDate)로 부터 imminentStand일 이내로 남은 Food들을 조회한다.")
    void findFoodsWithin7DaysOfItsExpiryDate() {
        // given
        String memberKey = "memberKey";
        LocalDate imminentStand = LocalDate.now().plusDays(7);
        List<Pair<String, LocalDate>> foodInfos = List.of(
                new Pair<>("food1", LocalDate.now().plusDays(2)),
                new Pair<>("food2", LocalDate.now().plusDays(7)),
                new Pair<>("food3", LocalDate.now().plusDays(8))
        );
        foodInfos.forEach(foodInfo ->
                em.persist(FoodEntity.builder()
                        .name(foodInfo.first)
                        .imageUrl("https://test.com/image.jpg")
                        .storageMethod(StorageMethod.FROZEN)
                        .expiryDate(foodInfo.second)
                        .expiryAlarmDays(2)
                        .memo("")
                        .selectedCategoryCount(1)
                        .memberKey(memberKey)
                        .build()));

        // when
        List<FoodEntity> imminentFoods = foodRepository.findImminentFoods(imminentStand, memberKey);

        // then
        assertThat(imminentFoods).hasSize(2);
    }

    @Test
    @DisplayName("foodId와 memberKey로 해당 Food를 조회한다.")
    void findFoodByIdAndMemberKey() {
        // given
        String memberKey = "memberKey";
        List<Pair<String, String>> foodInfos = List.of(
                new Pair<>("food2", memberKey),
                new Pair<>("food3", "anotherMemberKey")
        );
        Long foodId = em.persist(FoodEntity.builder()
                .name("food1")
                .imageUrl("https://test.com/image.jpg")
                .storageMethod(StorageMethod.FROZEN)
                .expiryDate(LocalDate.now())
                .expiryAlarmDays(2)
                .memo("")
                .selectedCategoryCount(1)
                .memberKey(memberKey)
                .build()).getId();
        foodInfos.forEach(foodInfo ->
                        em.persist(FoodEntity.builder()
                                .name(foodInfo.first)
                                .imageUrl("https://test.com/image.jpg")
                                .storageMethod(StorageMethod.FROZEN)
                                .expiryDate(LocalDate.now())
                                .expiryAlarmDays(2)
                                .memo("")
                                .selectedCategoryCount(1)
                                .memberKey(foodInfo.second)
                                .build()));

        // when
        Optional<FoodEntity> food = foodRepository.findByIdAndMemberKey(foodId, memberKey);

        // then
        assertThat(food).isNotEmpty();
        assertThat(food.get().getName()).isEqualTo("food1");
    }

    @Test
    @DisplayName("memberKey로 해당 Member의 Food 개수를 조회한다.")
    void findFoodCount() {
        // given
        String memberKey = "memberKey";
        List<Pair<String, String>> foodInfos = List.of(
                new Pair<>("food1", memberKey),
                new Pair<>("food2", memberKey),
                new Pair<>("food3", "anotherMemberKey")
        );
        foodInfos.forEach(foodInfo ->
                        em.persist(FoodEntity.builder()
                                .name(foodInfo.first)
                                .imageUrl("https://test.com/image.jpg")
                                .storageMethod(StorageMethod.FROZEN)
                                .expiryDate(LocalDate.now())
                                .expiryAlarmDays(2)
                                .memo("")
                                .selectedCategoryCount(1)
                                .memberKey(foodInfo.second)
                                .build()));

        // when
        long foodCount = foodRepository.foodCount(memberKey);

        // then
        assertThat(foodCount).isEqualTo(2);
    }
}