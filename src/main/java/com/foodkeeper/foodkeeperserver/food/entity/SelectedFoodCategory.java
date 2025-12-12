package com.foodkeeper.foodkeeperserver.food.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

}
