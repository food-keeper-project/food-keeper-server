package com.foodkeeper.foodkeeperserver.bookmarkedfood.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.bookmarkedfood.dataaccess.entity.BookmarkedFoodEntity;
import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;
import com.foodkeeper.foodkeeperserver.support.repository.QuerydslRepositorySupport;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.List;

import static com.foodkeeper.foodkeeperserver.bookmarkedfood.dataaccess.entity.QBookmarkedFoodEntity.bookmarkedFoodEntity;

public class BookmarkedFoodRepositoryCustomImpl extends QuerydslRepositorySupport
        implements BookmarkedFoodRepositoryCustom {

    protected BookmarkedFoodRepositoryCustomImpl() {
        super(BookmarkedFoodEntity.class);
    }

    @Override
    public SliceObject<BookmarkedFoodEntity> findBookmarkedFoods(Cursorable<Long> cursorable, String memberKey) {
        List<BookmarkedFoodEntity> content = selectFrom(bookmarkedFoodEntity)
                .where(ltCursor(cursorable.cursor()), bookmarkedFoodEntity.memberKey.eq(memberKey))
                .orderBy(bookmarkedFoodEntity.id.desc())
                .fetch();

        return new SliceObject<>(content, cursorable, hasNext(cursorable, content));
    }

    private static BooleanExpression ltCursor(Long cursor) {
        return cursor == null ? null : bookmarkedFoodEntity.id.lt(cursor);
    }
}
