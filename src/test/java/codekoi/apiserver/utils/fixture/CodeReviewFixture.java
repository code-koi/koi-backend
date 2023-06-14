package codekoi.apiserver.utils.fixture;

import codekoi.apiserver.domain.code.review.domain.CodeReview;
import codekoi.apiserver.domain.code.review.domain.CodeReviewStatus;
import codekoi.apiserver.domain.user.domain.User;

public enum CodeReviewFixture {

    REVIEW("자바 스프링 관련 코드 질문합니다.", "이 코드에서 무언가 문제가 있는 것 같습니다. 실행할 때, 해당 빈을 찾을 수 없다고 합니다.",
            CodeReviewStatus.PENDING),


    ;

    public final String title;
    public final String content;
    public final CodeReviewStatus status;

    CodeReviewFixture(String title, String content, CodeReviewStatus status) {
        this.title = title;
        this.content = content;
        this.status = status;
    }

    public CodeReview toCodeReview(User user) {
        return CodeReview.builder()
                .user(user)
                .title(title)
                .content(content)
                .status(status)
                .build();
    }
}
