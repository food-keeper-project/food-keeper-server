package com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity;

import com.foodkeeper.foodkeeperserver.recipe.domain.RecipeIngredient;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "recipe_ingredient")
public class RecipeIngredientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_ingredient_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String quantity;

    @Column(nullable = false)
    private Long recipeId;

    @Builder
    public RecipeIngredientEntity(String name, String quantity, Long recipeId) {
        this.name = name;
        this.quantity = quantity;
        this.recipeId = recipeId;
    }

    public static RecipeIngredientEntity of(RecipeIngredient ingredient, Long recipeId) {
        return RecipeIngredientEntity.builder()
                .name(ingredient.name())
                .quantity(ingredient.quantity())
                .recipeId(recipeId)
                .build();
    }

    public RecipeIngredient toDomain() {
        return new RecipeIngredient(name, quantity);
    }
}
