package codekoi.apiserver.domain.code.review.domain;

import codekoi.apiserver.domain.model.TimeBaseEntity;
import codekoi.apiserver.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Favorite extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "code_review_id")
    private CodeReview codeReview;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    private Favorite(Long id, CodeReview codeReview, User user) {
        this.id = id;
        this.codeReview = codeReview;
        this.user = user;
    }

    public static Favorite of(CodeReview codeReview, User user) {
        return Favorite.builder()
                .codeReview(codeReview)
                .user(user)
                .build();
    }
}
