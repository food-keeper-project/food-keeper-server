package com.foodkeeper.foodkeeperserver.food.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.enums.EntityStatus;
import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.support.repository.QuerydslRepositorySupport;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.foodkeeper.foodkeeperserver.food.dataaccess.entity.QFoodEntity.foodEntity;
import static com.foodkeeper.foodkeeperserver.food.dataaccess.entity.QSelectedFoodCategoryEntity.selectedFoodCategoryEntity;
import static com.querydsl.core.types.dsl.Expressions.dateTemplate;

public class FoodRepositoryCustomImpl extends QuerydslRepositorySupport implements FoodRepositoryCustom {

    protected FoodRepositoryCustomImpl() {
        super(FoodEntity.class);
    }

    @Override
    public SliceObject<FoodEntity> findFoods(Cursorable<Long> cursorable,
                                             Long categoryId,
                                             String memberKey) {

        List<FoodEntity> content = selectFrom(foodEntity)
                .innerJoin(selectedFoodCategoryEntity)
                .on(foodEntity.id.eq(selectedFoodCategoryEntity.foodId))
                .where(selectedFoodCategoryEntity.foodCategoryId.eq(categoryId))
                .where(eqMember(memberKey), isActive())
                .where(ltCursor(cursorable.cursor()))
                .orderBy(foodEntity.id.desc())
                .limit(cursorable.limit() + 1)
                .fetch();

        return new SliceObject<>(content, cursorable, hasNext(cursorable, content));
    }

    private BooleanExpression ltCursor(Long cursor) {
        return cursor == null ? null : foodEntity.id.lt(cursor);
    }

    @Override
    public List<FoodEntity> findAllByMemberKey(String memberKey) {

        return selectFrom(foodEntity)
                .where(eqMember(memberKey), isActive())
                .orderBy(
                        foodEntity.name.asc(),
                        foodEntity.createdAt.desc()
                )
                .fetch();
    }


    @Override
    public List<FoodEntity> findImminentFoods(LocalDate imminentStand, String memberKey) {
        return selectFrom(foodEntity)
                .where(eqMember(memberKey), isActive())
                .where(foodEntity.expiryDate.loe(imminentStand), foodEntity.expiryDate.goe(LocalDate.now()))
                .orderBy(foodEntity.expiryDate.asc())
                .fetch();
    }

    @Override
    public List<FoodEntity> findFoodsToNotify(LocalDate targetDate) {
        return selectFrom(foodEntity)
                .where(isActive(),
                        dateTemplate(
                                Integer.class,
                                "DATEDIFF({0}, {1})",
                                foodEntity.expiryDate,
                                targetDate
                        ).eq(foodEntity.expiryAlarmDays)
                )
                .fetch();
    }

    @Override
    public List<Long> removeFoods(String memberKey) {
        List<Long> foodIds = select(foodEntity.id)
                .from(foodEntity)
                .where(eqMember(memberKey), isActive())
                .fetch();

        update(foodEntity)
                .set(foodEntity.status, EntityStatus.DELETED)
                .set(foodEntity.deletedAt, LocalDateTime.now())
                .where(eqMember(memberKey), isActive())
                .execute();

        getEntityManager().clear();

        return foodIds;
    }

    @Override
    public Optional<FoodEntity> findByIdAndMemberKey(Long id, String memberKey) {
        return Optional.ofNullable(
                selectFrom(foodEntity)
                        .where(foodEntity.id.eq(id), eqMember(memberKey), isActive())
                        .fetchOne()
        );
    }

    @Override
    public long foodCount(String memberKey) {
        Long count = select(foodEntity.id.count())
                .from(foodEntity)
                .where(eqMember(memberKey), isActive())
                .fetchOne();
        return count != null ? count : 0L;
    }

    private static BooleanExpression eqMember(String memberKey) {
        return foodEntity.memberKey.eq(memberKey);
    }

    private static BooleanExpression isActive() {
        return foodEntity.status.eq(EntityStatus.ACTIVE);
    }
}
