package com.foodkeeper.foodkeeperserver.food.dataaccess.entity;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.BaseEntity;
import com.foodkeeper.foodkeeperserver.food.domain.FoodCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "food_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FoodCategoryEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_category_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String memberKey;

    @Builder
    private FoodCategoryEntity(String name, String memberKey) {
        this.name = name;
        this.memberKey = memberKey;
    }

    public FoodCategory toDomain() {
        return new FoodCategory(
                this.id,
                this.name,
                this.memberKey
        );
    }

    public static FoodCategoryEntity from(FoodCategory foodCategory) {
        return FoodCategoryEntity.builder()
                .name(foodCategory.name())
                .memberKey(foodCategory.memberId())
                .build();
    }
}
