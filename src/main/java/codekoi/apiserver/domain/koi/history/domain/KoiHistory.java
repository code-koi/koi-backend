package codekoi.apiserver.domain.koi.history.domain;

import codekoi.apiserver.domain.code.comment.domain.CodeReviewComment;
import codekoi.apiserver.domain.koi.domain.KoiType;
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
public class KoiHistory extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "koi_history_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "target_users_id")
    private User target;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "source_users_id")
    private User source;

    @Enumerated(EnumType.STRING)
    private KoiType koiType;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "code_review_comment_id")
    private CodeReviewComment codeReviewComment;

    private String message;

    @Builder
    private KoiHistory(Long id, User target, User source, KoiType koiType, CodeReviewComment codeReviewComment, String message) {
        this.id = id;
        this.target = target;
        this.source = source;
        this.koiType = koiType;
        this.codeReviewComment = codeReviewComment;
        this.message = message;
    }

    public static KoiHistory of(User target, User source, CodeReviewComment comment, KoiType koiType, String message) {
        return KoiHistory.builder()
                .target(target)
                .source(source)
                .codeReviewComment(comment)
                .koiType(koiType)
                .message(message)
                .build();
    }
}
