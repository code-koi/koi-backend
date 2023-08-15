package com.codekoi.domain.comment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
class ReviewCommentRepositoryCustomImpl implements ReviewCommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final QReviewComment comment = QReviewComment.reviewComment;


    @Override
    public List<ReviewComment> hotCommentRank() {
        return queryFactory.select(comment)
                .where(comment.canceledAt.isNull())
                .orderBy(comment.likeCount.desc())
                .orderBy(comment.createdAt.desc())
                .limit(5)
                .fetch();
    }
}
