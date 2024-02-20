package com.codekoi.codereview.fixture;

import com.codekoi.review.CodeReview;
import com.codekoi.review.ReviewComment;
import com.codekoi.user.User;

public enum ReviewCommentFixture {
    REVIEW_COMMENT("이 코드에서 생성자가 조금 이상합니다. 다시 확인해주세요.")

    ;
    public final String content;

    ReviewCommentFixture(String content) {
        this.content = content;
    }

    public ReviewComment toCodeReviewComment(User user, CodeReview codeReview) {
        return toCodeReviewChildComment(null, user, codeReview, null);
    }

    public ReviewComment toCodeReviewComment(Long id, User user, CodeReview codeReview) {
        return toCodeReviewChildComment(id, user, codeReview, null);
    }

    public ReviewComment toCodeReviewChildComment(User user, CodeReview codeReview, ReviewComment parentComment) {
        return toCodeReviewChildComment(null, user, codeReview, parentComment);
    }

    public ReviewComment toCodeReviewChildComment(Long id, User user, CodeReview codeReview, ReviewComment parentComment) {
        return ReviewComment.builder()
                .id(id)
                .codeReview(codeReview)
                .user(user)
                .content(content)
                .parentComment(parentComment)
                .build();
    }
}
