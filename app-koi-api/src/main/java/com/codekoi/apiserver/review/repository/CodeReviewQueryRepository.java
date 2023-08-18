package com.codekoi.apiserver.review.repository;

import com.codekoi.apiserver.review.dto.CodeReviewListCondition;
import com.codekoi.domain.review.CodeReview;
import com.codekoi.domain.review.CodeReviewStatus;
import com.codekoi.domain.review.QCodeReview;
import com.codekoi.domain.skill.review.QCodeReviewSkill;
import com.codekoi.domain.skill.skill.QSkill;
import com.codekoi.domain.user.QUser;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CodeReviewQueryRepository {

    private final JPAQueryFactory queryFactory;

    private final QCodeReview codeReview = QCodeReview.codeReview;
    private final QCodeReviewSkill codeReviewSkill = QCodeReviewSkill.codeReviewSkill;
    private final QSkill skill = QSkill.skill;
    private final QUser user = QUser.user;

    public List<CodeReview> getReviewList(CodeReviewListCondition condition) {
        int MAX_SIZE = 40;

        // refactor:  Dto Projection 변경?
        return queryFactory.selectFrom(codeReview)
                .where(getWhere(condition))
                .orderBy(codeReview.createdAt.desc())
                .limit(MAX_SIZE + 1)
                .fetch();
    }

    private Predicate[] getWhere(CodeReviewListCondition condition) {
        final List<String> tags = condition.getTag()
                .stream()
                .filter(StringUtils::hasText)
                .toList();

        if (tags.isEmpty()) {
            return new Predicate[]{
                    pendingEq(condition.getPending()),
                    resolvedEq(condition.getResolved()),
                    reviewIdLt(condition.getLastReviewId()),
                    keywordContain(condition.getKeyword())
            };
        }

        return new Predicate[]{
                pendingEq(condition.getPending()),
                resolvedEq(condition.getResolved()),
                reviewIdLt(condition.getLastReviewId()),
                keywordContain(condition.getKeyword()),

                codeReview.id.in(
                        JPAExpressions.select(codeReviewSkill.codeReview.id)
                                .from(codeReviewSkill)
                                .where(codeReviewSkill.skill.id.in(
                                                JPAExpressions.select(skill.id)
                                                        .from(skill)
                                                        .where(skill.name.in(tags))
                                        )
                                )
                )
        };
    }

    private BooleanExpression keywordContain(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return null;
        }
        return codeReview.content.contains(keyword);
    }

    private BooleanExpression reviewIdLt(Long lastReviewId) {
        if (lastReviewId == null) {
            return null;
        }

        return codeReview.id.lt(lastReviewId);
    }

    private BooleanExpression pendingEq(Boolean pending) {
        if (pending == null) {
            return null;
        }

        return codeReview.status.eq(CodeReviewStatus.PENDING);
    }

    private BooleanExpression resolvedEq(Boolean resolved) {
        if (resolved == null) {
            return null;
        }

        return codeReview.status.eq(CodeReviewStatus.RESOLVED);
    }
}
