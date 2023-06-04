package codekoi.apiserver.domain.koi.history.domain;

import codekoi.apiserver.domain.code.comment.domain.CodeReviewComment;
import codekoi.apiserver.domain.koi.domain.Koi;
import codekoi.apiserver.domain.model.TimeBaseEntity;
import codekoi.apiserver.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "code_review_comment_id")
    private CodeReviewComment codeReviewComment;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "koi_id")
    private Koi koi;

    private String message;
}
