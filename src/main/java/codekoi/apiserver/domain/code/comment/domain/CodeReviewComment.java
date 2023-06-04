package codekoi.apiserver.domain.code.comment.domain;

import codekoi.apiserver.domain.code.review.domain.CodeReview;
import codekoi.apiserver.domain.model.TimeBaseEntity;
import codekoi.apiserver.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CodeReviewComment extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_review_comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_review_id")
    private CodeReview codeReview;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_reviewer_users_id")
    private User user;

    private String content;
}
