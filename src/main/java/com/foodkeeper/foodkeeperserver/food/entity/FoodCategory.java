package com.foodkeeper.foodkeeperserver.food.entity;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
