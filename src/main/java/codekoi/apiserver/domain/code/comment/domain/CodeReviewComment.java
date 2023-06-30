package codekoi.apiserver.domain.code.comment.domain;

import codekoi.apiserver.domain.code.like.domain.Like;
import codekoi.apiserver.domain.code.review.domain.CodeReview;
import codekoi.apiserver.domain.model.TimeBaseEntity;
import codekoi.apiserver.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    private int likeCount;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    @Builder
    private CodeReviewComment(Long id, CodeReview codeReview, User user, String content, int likeCount, List<Like> likes) {
        this.id = id;
        this.codeReview = codeReview;
        this.user = user;
        this.content = content;
        this.likeCount = likeCount;
        this.likes = Objects.requireNonNullElse(likes, new ArrayList<>());
    }

    public static CodeReviewComment of(User user, CodeReview codeReview, String content) {
        return CodeReviewComment.builder()
                .user(user)
                .codeReview(codeReview)
                .content(content)
                .build();
    }

    public void addLikeCount() {
        this.likeCount += 1;
    }

    public void minusLikeCount() {
        this.likeCount -= 1;
    }
}
