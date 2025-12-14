package com.foodkeeper.foodkeeperserver.food.dataaccess.entity;

import com.foodkeeper.foodkeeperserver.food.domain.SelectedFoodCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "selected_food_category")
public class SelectedFoodCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "selected_food_category_id")
    private Long id;

    @Column(name = "food_id",nullable = false)
    private Long foodId;

    @Column(name = "food_category_id",nullable = false)
    private Long foodCategoryId;

    @Builder
    private SelectedFoodCategoryEntity(Long foodId, Long foodCategoryId){
        this.foodId = foodId;
        this.foodCategoryId = foodCategoryId;
    }

    public SelectedFoodCategory toDomain(){
        return new SelectedFoodCategory(
                this.id,
                this.foodId,
                this.foodCategoryId
        );
    }
    public static SelectedFoodCategoryEntity from(SelectedFoodCategory selectedFoodCategory){
        return SelectedFoodCategoryEntity.builder()
                .foodId(selectedFoodCategory.foodId())
                .foodCategoryId(selectedFoodCategory.foodCategoryId())
                .build();
    }
}
