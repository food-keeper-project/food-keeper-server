package com.foodkeeper.foodkeeperserver.bookmarkedfood.dataaccess.entity;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.BaseEntity;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.domain.StorageMethod;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "bookmarked_food")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkedFoodEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmarked_food_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false, length = 20)
    private StorageMethod storageMethod;

    @Column(nullable = false)
    private String memberKey;

    @Builder
    public BookmarkedFoodEntity(String name,
                                String imageUrl,
                                StorageMethod storageMethod,
                                String memberKey) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.storageMethod = storageMethod;
        this.memberKey = memberKey;
    }

    public static BookmarkedFoodEntity from(Food food, String memberKey) {
        return BookmarkedFoodEntity.builder()
                .name(food.name())
                .imageUrl(food.imageUrl())
                .storageMethod(food.storageMethod())
                .memberKey(memberKey)
                .build();
    }
}
