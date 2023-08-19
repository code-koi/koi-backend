package com.codekoi.apiserver.review.repository;

import com.codekoi.apiserver.review.dto.CodeReviewListCondition;
import com.codekoi.domain.review.CodeReview;
import com.codekoi.domain.review.CodeReviewStatus;
import com.codekoi.domain.review.QCodeReview;
import com.codekoi.domain.skill.review.QCodeReviewSkill;
import com.codekoi.domain.skill.skill.QSkill;
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

    public List<CodeReview> getReviewList(CodeReviewListCondition condition, int pageSize) {
        return queryFactory.selectFrom(codeReview)
                .where(statusEq(condition.getStatus()),
                        reviewIdLt(condition.getNextId()),
                        titleContains(condition.getTitle()),
                        tagContains(condition.getTag()))
                .orderBy(codeReview.createdAt.desc())
                .limit(pageSize + 1)
                .fetch();
    }

    private BooleanExpression tagContains(List<String> tags) {
        tags = tags.stream()
                .filter(StringUtils::hasText)
                .toList();

        if (tags.isEmpty()) {
            return null;
        }

        return codeReview.id.in(
                JPAExpressions.select(codeReviewSkill.codeReview.id)
                        .from(codeReviewSkill)
                        .where(codeReviewSkill.skill.id.in(
                                        JPAExpressions.select(skill.id)
                                                .from(skill)
                                                .where(skill.name.in(tags))
                                )
                        )
        );
    }

    private BooleanExpression titleContains(String title) {
        if (!StringUtils.hasText(title)) {
            return null;
        }
        return codeReview.title.contains(title);
    }

    private BooleanExpression reviewIdLt(Long nextReviewId) {
        if (nextReviewId == null) {
            return null;
        }

        return codeReview.id.lt(nextReviewId);
    }

    private BooleanExpression statusEq(CodeReviewStatus status) {
        if (status == null) {
            return null;
        }

        return codeReview.status.eq(status);
    }
}
