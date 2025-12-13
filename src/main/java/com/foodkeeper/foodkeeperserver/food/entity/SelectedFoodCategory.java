package com.foodkeeper.foodkeeperserver.food.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "selected_food_category")
public class SelectedFoodCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "selected_food_category_id")
    private Long id;

    @Column(name = "food_id",nullable = false)
    private Long foodId;

    @Column(name = "food_category_id",nullable = false)
    private Long foodCategoryId;

    @Builder
    private SelectedFoodCategory(Long foodId, Long foodCategoryId){
        this.foodId = foodId;
        this.foodCategoryId = foodCategoryId;
    }

    public static SelectedFoodCategory create(Long foodId, Long foodCategoryId) {
        return SelectedFoodCategory.builder()
                .foodId(foodId)
                .foodCategoryId(foodCategoryId)
                .build();
    }

}
