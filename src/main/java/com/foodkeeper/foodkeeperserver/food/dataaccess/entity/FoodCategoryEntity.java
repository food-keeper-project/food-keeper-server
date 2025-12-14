package com.foodkeeper.foodkeeperserver.food.dataaccess.entity;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.BaseEntity;
import com.foodkeeper.foodkeeperserver.food.domain.FoodCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FoodCategoryEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_category_id")
    private Long id;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "member_id", nullable = false)
    private String memberId;

    @Builder
    private FoodCategoryEntity(String name, String memberId){
        this.name = name;
        this.memberId = memberId;
    }

    public FoodCategory toDomain(){
        return new FoodCategory(
                this.id,
                this.name,
                this.memberId
        );
    }
}
