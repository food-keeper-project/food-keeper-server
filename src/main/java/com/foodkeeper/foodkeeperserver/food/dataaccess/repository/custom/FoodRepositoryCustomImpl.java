package com.foodkeeper.foodkeeperserver.food.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.enums.EntityStatus;
import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.support.repository.QuerydslRepositorySupport;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.foodkeeper.foodkeeperserver.food.dataaccess.entity.QFoodEntity.foodEntity;
import static com.foodkeeper.foodkeeperserver.food.dataaccess.entity.QSelectedFoodCategoryEntity.selectedFoodCategoryEntity;

public class FoodRepositoryCustomImpl extends QuerydslRepositorySupport implements FoodRepositoryCustom {

    protected FoodRepositoryCustomImpl() {
        super(FoodEntity.class);
    }

    @Override
    public SliceObject<FoodEntity> findFoods(Cursorable<Long> cursorable,
                                             Long categoryId,
                                             String memberKey) {

        JPAQuery<FoodEntity> query = selectFrom(foodEntity);

        applyCategoryFilter(query, categoryId);

        List<FoodEntity> content = query
                .where(eqMember(memberKey), isNotDeleted())
                .where(gtCursor(cursorable.cursor()))
                .orderBy(foodEntity.expiryDate.asc(), foodEntity.id.asc())
                .limit(cursorable.limit() + 1)
                .fetch();

        return new SliceObject<>(content, cursorable, hasNext(cursorable, content));
    }

    private BooleanExpression gtCursor(Long cursor) {
        if (cursor == null) {
            return null;
        }

        LocalDate cursorExpiryDate = getCursorExpiryDate(cursor);
        return foodEntity.expiryDate.gt(cursorExpiryDate)
                .or(foodEntity.expiryDate.eq(cursorExpiryDate).and(foodEntity.id.gt(cursor)));
    }

    private LocalDate getCursorExpiryDate(Long cursor) {
        return select(foodEntity.expiryDate)
                .from(foodEntity)
                .where(foodEntity.id.eq(cursor))
                .fetchOne();
    }

    private void applyCategoryFilter(JPAQuery<FoodEntity> query, Long categoryId) {
        if (categoryId != null) {
            query.join(selectedFoodCategoryEntity)
                    .on(foodEntity.id.eq(selectedFoodCategoryEntity.foodId))
                    .where(selectedFoodCategoryEntity.foodCategoryId.eq(categoryId));
        }
    }

    @Override
    public List<FoodEntity> findAllByMemberKey(String memberKey) {

        return selectFrom(foodEntity)
                .where(eqMember(memberKey), isNotDeleted())
                .orderBy(
                        foodEntity.name.asc(),
                        foodEntity.createdAt.desc()
                )
                .fetch();
    }


    @Override
    public List<FoodEntity> findImminentFoods(LocalDate imminentStand, String memberKey) {
        return selectFrom(foodEntity)
                .where(eqMember(memberKey), isNotDeleted())
                .where(foodEntity.expiryDate.loe(imminentStand), foodEntity.expiryDate.goe(LocalDate.now()))
                .orderBy(foodEntity.expiryDate.asc())
                .fetch();
    }

    @Override
    public List<Long> removeFoods(String memberKey) {
        List<Long> foodIds = select(foodEntity.id)
                .from(foodEntity)
                .where(eqMember(memberKey), isNotDeleted())
                .fetch();

        update(foodEntity)
                .set(foodEntity.status, EntityStatus.DELETED)
                .set(foodEntity.deletedAt, LocalDateTime.now())
                .where(eqMember(memberKey), isNotDeleted())
                .execute();

        return foodIds;
    }

    @Override
    public Optional<FoodEntity> findByIdAndMemberKey(Long id, String memberKey) {
        return Optional.ofNullable(
                selectFrom(foodEntity)
                        .where(foodEntity.id.eq(id), eqMember(memberKey), isNotDeleted())
                        .fetchOne()
        );
    }

    @Override
    public long foodCount(String memberKey) {
        Long count = select(foodEntity.id.count())
                .from(foodEntity)
                .where(eqMember(memberKey), isNotDeleted())
                .fetchOne();
        return count != null ? count : 0L;
    }

    private static BooleanExpression eqMember(String memberKey) {
        return foodEntity.memberKey.eq(memberKey);
    }

    private static BooleanExpression isNotDeleted() {
        return foodEntity.status.ne(EntityStatus.DELETED);
    }
}
