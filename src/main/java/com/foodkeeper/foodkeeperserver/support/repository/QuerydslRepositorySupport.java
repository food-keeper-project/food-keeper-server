package com.foodkeeper.foodkeeperserver.support.repository;

import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.SimpleEntityPathResolver;

import java.util.List;

@Getter(AccessLevel.PROTECTED)
public abstract class QuerydslRepositorySupport {

    private final Class<?> entityClass;
    private Querydsl querydsl;
    private EntityManager entityManager;
    private JPAQueryFactory jpaQueryFactory;

    protected QuerydslRepositorySupport(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;

        EntityPath<?> path = SimpleEntityPathResolver.INSTANCE.createPath(
                JpaEntityInformationSupport.getEntityInformation(entityClass, entityManager).getJavaType());

        this.querydsl = new Querydsl(entityManager, new PathBuilder<>(path.getType(), path.getMetadata()));
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    protected <T> JPAQuery<T> select(EntityPath<T> select) {
        return this.jpaQueryFactory.select(select);
    }

    protected <T> JPAQuery<T> select(Expression<T> select) {
        return this.jpaQueryFactory.select(select);
    }

    protected <T> JPAQuery<T> selectFrom(EntityPath<T> from) {
        return this.jpaQueryFactory.selectFrom(from);
    }

    protected JPAQuery<Integer> selectOne() {
        return this.jpaQueryFactory.selectOne();
    }

    protected <T> JPAUpdateClause update(EntityPath<T> from) {
        return this.jpaQueryFactory.update(from);
    }

    protected <T> boolean hasNext(Cursorable<?> cursorable, List<T> content) {
        if (content.size() > cursorable.limit()) {
            content.removeLast();
            return true;
        }
        return false;
    }
}
