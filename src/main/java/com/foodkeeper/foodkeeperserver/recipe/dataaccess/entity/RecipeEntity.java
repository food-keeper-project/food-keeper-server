package com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.BaseEntity;
import com.foodkeeper.foodkeeperserver.recipe.domain.Recipe;
import com.foodkeeper.foodkeeperserver.recipe.domain.RecipeIngredient;
import com.foodkeeper.foodkeeperserver.recipe.domain.RecipeStep;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "recipe")
public class RecipeEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id")
    private Long id;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer cookMinutes;

    @Column(nullable = false)
    private String memberKey;

    @Builder
    public RecipeEntity(String title, String description, Integer cookMinutes, String memberKey) {
        this.title = title;
        this.description = description;
        this.cookMinutes = cookMinutes;
        this.memberKey = memberKey;
    }

    public static RecipeEntity of(Recipe recipe, String memberKey) {
        return RecipeEntity.builder()
                .title(recipe.menuName())
                .description(recipe.description())
                .cookMinutes(recipe.cookMinutes())
                .memberKey(memberKey)
                .build();
    }

    public Recipe toDomain() {
        return Recipe.builder()
                .menuName(title)
                .description(description)
                .cookMinutes(cookMinutes)
                .steps(List.of())
                .ingredients(List.of())
                .build();
    }

    public Recipe toDomain(List<RecipeStep> steps, List<RecipeIngredient> ingredients) {
        return Recipe.builder()
                .menuName(title)
                .description(description)
                .cookMinutes(cookMinutes)
                .steps(steps)
                .ingredients(ingredients)
                .build();
    }
}
