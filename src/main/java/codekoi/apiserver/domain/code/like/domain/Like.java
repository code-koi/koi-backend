package codekoi.apiserver.domain.code.like.domain;

import codekoi.apiserver.domain.code.comment.domain.CodeReviewComment;
import codekoi.apiserver.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "likes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    private User user;

    @ManyToOne(fetch = LAZY)
    private CodeReviewComment comment;

    @Builder
    private Like(Long id, User user, CodeReviewComment comment) {
        this.id = id;
        this.user = user;
        this.comment = comment;
    }

    public static Like of(User user, CodeReviewComment comment) {
        return Like.builder()
                .user(user)
                .comment(comment)
                .build();
    }
}
