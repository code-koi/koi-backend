package com.codekoi.domain.favorite;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class FavoriteRepositoryCustomImpl implements FavoriteRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final QFavorite favorite = QFavorite.favorite;

    @Override
    public List<Long> hotReviewRank() {
        return queryFactory
                .select(favorite.codeReview.id)
                .from(favorite)
                .groupBy(favorite.codeReview.id)
                .orderBy(favorite.count().desc())
                .fetch();
    }
}
