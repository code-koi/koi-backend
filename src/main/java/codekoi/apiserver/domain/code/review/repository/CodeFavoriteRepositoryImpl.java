package codekoi.apiserver.domain.code.review.repository;


import codekoi.apiserver.domain.code.review.domain.QFavorite;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CodeFavoriteRepositoryImpl implements CodeFavoriteRepositoryCustom {

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
