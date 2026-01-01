package com.foodkeeper.foodkeeperserver.recipe.fixture;

import com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.RecipeEntity;

public enum RecipeEntityFixture {
    DEFAULT("title", "desc", 20)
    ;

    private final String title;
    private final String description;
    private final Integer cookMinutes;

    RecipeEntityFixture(String title, String description, Integer cookMinutes) {
        this.title = title;
        this.description = description;
        this.cookMinutes = cookMinutes;
    }

    public RecipeEntity get(String memberKey) {
        return RecipeEntity.builder()
                .title(title)
                .description(description)
                .cookMinutes(cookMinutes)
                .memberKey(memberKey)
                .build();
    }
}
