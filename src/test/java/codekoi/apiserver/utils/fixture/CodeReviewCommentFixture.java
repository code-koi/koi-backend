package codekoi.apiserver.utils.fixture;

import codekoi.apiserver.domain.code.comment.domain.CodeReviewComment;
import codekoi.apiserver.domain.code.review.domain.CodeReview;
import codekoi.apiserver.domain.user.domain.User;

public enum CodeReviewCommentFixture {
    REVIEW_COMMENT("이 코드에서 생성자가 조금 이상합니다. 다시 확인해주세요.")

    ;
    public final String content;

    CodeReviewCommentFixture(String content) {
        this.content = content;
    }

    public CodeReviewComment toCodeReviewComment(User user, CodeReview codeReview) {
        return CodeReviewComment.builder()
                .codeReview(codeReview)
                .user(user)
                .content(content)
                .build();
    }
}
