package com.foodkeeper.foodkeeperserver.food.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.enums.EntityStatus;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.SelectedFoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.custom.FoodCategoryResult;
import com.foodkeeper.foodkeeperserver.food.domain.StorageMethod;
import com.foodkeeper.foodkeeperserver.support.repository.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class FoodCategoryRepositoryTest extends RepositoryTest {

    @Autowired FoodCategoryRepository foodCategoryRepository;

    @Test
    @DisplayName("memberKey로 해당 멤버의 FoodCategory들을 삭제한다.")
    void deleteCategoriesByMemberKey() {
        // given
        String memberKey = "memberKey";
        FoodCategoryEntity category1 = em.persist(new FoodCategoryEntity("category1", memberKey));
        FoodCategoryEntity category2 = em.persist(new FoodCategoryEntity("category2", memberKey));
        FoodCategoryEntity category3 = em.persist(new FoodCategoryEntity("category3", "memberKey2"));

        // when
        foodCategoryRepository.deleteFoodCategories(memberKey);
        em.flush();
        em.clear();

        // then
        Optional<FoodCategoryEntity> foundCategory1 = foodCategoryRepository.findById(category1.getId());
        Optional<FoodCategoryEntity> foundCategory2 = foodCategoryRepository.findById(category2.getId());
        Optional<FoodCategoryEntity> foundCategory3 = foodCategoryRepository.findById(category3.getId());
        assertThat(foundCategory1).isNotEmpty();
        assertThat(foundCategory1.get().getStatus()).isEqualTo(EntityStatus.DELETED);
        assertThat(foundCategory1.get().getDeletedAt()).isNotNull();
        assertThat(foundCategory2).isNotEmpty();
        assertThat(foundCategory2.get().getStatus()).isEqualTo(EntityStatus.DELETED);
        assertThat(foundCategory2.get().getDeletedAt()).isNotNull();
        assertThat(foundCategory3).isNotEmpty();
        assertThat(foundCategory3.get().getStatus()).isEqualTo(EntityStatus.ACTIVE);
        assertThat(foundCategory3.get().getDeletedAt()).isNull();
    }

    @Test
    @DisplayName("memberKey로 해당 멤버의 모든 FoodCategory를 조회한다.")
    void findCategoriesByMemberKey() {
        // given
        String memberKey = "memberKey";
        em.persist(new FoodCategoryEntity("category1", memberKey));
        em.persist(new FoodCategoryEntity("category2", memberKey));
        em.persist(new FoodCategoryEntity("category2", "memberKey2"));

        // when
        List<FoodCategoryEntity> categories = foodCategoryRepository.findAllByMemberKey(memberKey);

        // then
        assertThat(categories).hasSize(2);
    }

    @Test
    @DisplayName("CategoryId와 memberKey로 FoodCategory를 조회한다.")
    void findCategoryByCategoryIdAndMemberKey() {
        // given
        String memberKey = "memberKey";
        FoodCategoryEntity category1 = em.persist(new FoodCategoryEntity("category1", memberKey));
        em.persist(new FoodCategoryEntity("category2", memberKey));
        em.persist(new FoodCategoryEntity("category2", "memberKey2"));

        // when
        Optional<FoodCategoryEntity> foundCategory = foodCategoryRepository
                .findByIdAndMemberKey(category1.getId(), memberKey);

        // then
        assertThat(foundCategory).isNotEmpty();
        assertThat(foundCategory.get().getName()).isEqualTo("category1");
    }

    @Test
    @DisplayName("CategoryId들에 해당하는 모든 FoodCategory를 조회한다.")
    void findCategoriesByCategoryIds() {
        String memberKey = "memberKey";
        FoodEntity food1 = em.persist(FoodEntity.builder()
                .name("name")
                .imageUrl("https://test.com/image.jpg")
                .storageMethod(StorageMethod.FROZEN)
                .expiryDate(LocalDate.now().plusDays(2))
                .expiryAlarmDays(2)
                .memo("")
                .selectedCategoryCount(1)
                .memberKey(memberKey)
                .build());
        FoodEntity food2 = em.persist(FoodEntity.builder()
                .name("name")
                .imageUrl("https://test.com/image.jpg")
                .storageMethod(StorageMethod.FROZEN)
                .expiryDate(LocalDate.now().plusDays(2))
                .expiryAlarmDays(2)
                .memo("")
                .selectedCategoryCount(1)
                .memberKey(memberKey)
                .build());
        FoodCategoryEntity category1 = em.persist(new FoodCategoryEntity("category1", memberKey));
        FoodCategoryEntity category2 = em.persist(new FoodCategoryEntity("category2", memberKey));
        FoodCategoryEntity category3 = em.persist(new FoodCategoryEntity("category3", memberKey));
        em.persist(new SelectedFoodCategoryEntity(food1.getId(), category1.getId()));
        em.persist(new SelectedFoodCategoryEntity(food1.getId(), category3.getId()));
        em.persist(new SelectedFoodCategoryEntity(food2.getId(), category2.getId()));

        List<FoodCategoryResult> categories1 = foodCategoryRepository.findAllByIdIn(List.of(food1.getId()));
        List<FoodCategoryResult> categories2 = foodCategoryRepository.findAllByIdIn(List.of(food1.getId(), food2.getId()));

        assertThat(categories1).hasSize(2);
        assertThat(categories1.stream().map(FoodCategoryResult::foodCategoryId).toList())
                .contains(category1.getId(), category3.getId());
        assertThat(categories2).hasSize(3);
        assertThat(categories2.stream().map(FoodCategoryResult::foodCategoryId).toList())
                .contains(category1.getId(), category2.getId(), category3.getId());
    }
}