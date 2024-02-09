package com.codekoi.apiserver.review.repository;

import com.codekoi.apiserver.review.dto.CodeReviewListCondition;
import com.codekoi.review.CodeReview;
import com.codekoi.review.CodeReviewStatus;
import com.codekoi.review.QCodeReview;
import com.codekoi.review.QCodeReviewSkill;
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

    public List<CodeReview> getReviewList(CodeReviewListCondition condition, int pageSize) {
        return queryFactory.selectFrom(codeReview)
                .where(reviewIdLt(condition.getLastId()),
                        statusEq(condition.getStatus()),
                        titleContains(condition.getTitle()),
                        tagContains(condition.getSkillIds()))
                .orderBy(codeReview.createdAt.desc())
                .limit(pageSize + 1)
                .fetch();
    }

    private BooleanExpression tagContains(List<Long> skillIds) {
        if (skillIds.isEmpty()) {
            return null;
        }

        return codeReview.id.in(
                JPAExpressions.select(codeReviewSkill.codeReview.id)
                        .from(codeReviewSkill)
                        .where(codeReviewSkill.skill.id.in(skillIds))
        );
    }

    private BooleanExpression titleContains(String title) {
        if (!StringUtils.hasText(title)) {
            return null;
        }

        return codeReview.title.contains(title);
    }

    private BooleanExpression reviewIdLt(Long lastReviewId) {
        if (lastReviewId == null) {
            return null;
        }

        return codeReview.id.lt(lastReviewId);
    }

    private BooleanExpression statusEq(CodeReviewStatus status) {
        if (status == null) {
            return null;
        }

        return codeReview.status.eq(status);
    }
}
