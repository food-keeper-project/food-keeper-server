package com.foodkeeper.foodkeeperserver.food.dataaccess.entity;

import com.foodkeeper.foodkeeperserver.food.domain.SelectedFoodCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "selected_food_category")
public class SelectedFoodCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "selected_food_category_id")
    private Long id;

    @Column(nullable = false)
    private Long foodId;

    @Column(nullable = false)
    private Long foodCategoryId;

    @Builder
    public SelectedFoodCategoryEntity(Long foodId, Long foodCategoryId) {
        this.foodId = foodId;
        this.foodCategoryId = foodCategoryId;
    }

    public SelectedFoodCategory toDomain() {
        return new SelectedFoodCategory(
                this.id,
                this.foodId,
                this.foodCategoryId
        );
    }

    public static SelectedFoodCategoryEntity from(SelectedFoodCategory selectedFoodCategory) {
        return SelectedFoodCategoryEntity.builder()
                .foodId(selectedFoodCategory.foodId())
                .foodCategoryId(selectedFoodCategory.foodCategoryId())
                .build();
    }
}
