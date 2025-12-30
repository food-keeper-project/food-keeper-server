package com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity;

import com.foodkeeper.foodkeeperserver.recipe.domain.RecipeStep;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "recipe_step")
public class RecipeStepEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_step_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long recipeId;

    @Builder
    public RecipeStepEntity(String title, String content, Long recipeId) {
        this.title = title;
        this.content = content;
        this.recipeId = recipeId;
    }

    public static RecipeStepEntity of(RecipeStep recipeStep, Long recipeId) {
        return RecipeStepEntity.builder()
                .title(recipeStep.title())
                .content(recipeStep.content())
                .recipeId(recipeId)
                .build();
    }

    public RecipeStep toDomain() {
        return new RecipeStep(title, content);
    }
}
