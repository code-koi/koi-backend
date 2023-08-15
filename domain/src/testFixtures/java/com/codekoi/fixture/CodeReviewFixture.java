package com.codekoi.fixture;


import com.codekoi.domain.review.CodeReview;
import com.codekoi.domain.review.CodeReviewStatus;
import com.codekoi.domain.user.User;

public enum CodeReviewFixture {

    REVIEW1("자바 스프링 관련 코드 질문합니다.", "이 코드에서 무언가 문제가 있는 것 같습니다. 실행할 때, 해당 빈을 찾을 수 없다고 합니다.",
            CodeReviewStatus.PENDING),
    REVIEW2("JPA에 대해서 질문 드립니다!", "JPA를 사용하는데, 뭔가 이상합니다. 트랜젝션이 정상적으로 작동하지 않습니다.",
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

    public CodeReview toCodeReview(Long id, User user) {
        return CodeReview.builder()
                .id(id)
                .user(user)
                .title(title)
                .content(content)
                .status(status)
                .build();
    }
}
