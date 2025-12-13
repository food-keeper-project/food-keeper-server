package com.foodkeeper.foodkeeperserver.food.entity;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "food_category")
public class FoodCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_category_id")
    private Long id;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "member_id", nullable = false)
    private String memberId;

    @Builder
    private FoodCategory(String name, String memberId){
        this.name = name;
        this.memberId = memberId;
    }

    public static FoodCategory create(String name, String memberId){
        return FoodCategory.builder()
                .name(name)
                .memberId(memberId)
                .build();
    }
}
