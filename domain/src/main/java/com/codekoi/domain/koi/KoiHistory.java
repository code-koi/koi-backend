package com.codekoi.domain.koi;

import com.codekoi.domain.comment.ReviewComment;
import com.codekoi.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "koi_history")
public class KoiHistory {

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
    @JoinColumn(name = "review_comment_id")
    private ReviewComment codeReviewComment;

    private String message;

    protected KoiHistory() {
    }

    @Builder
    private KoiHistory(Long id, User target, User source, KoiType koiType, ReviewComment codeReviewComment, String message) {
        this.id = id;
        this.target = target;
        this.source = source;
        this.koiType = koiType;
        this.codeReviewComment = codeReviewComment;
        this.message = message;
    }

    public static KoiHistory of(User target, User source, ReviewComment comment, KoiType koiType, String message) {
        return KoiHistory.builder()
                .target(target)
                .source(source)
                .codeReviewComment(comment)
                .koiType(koiType)
                .message(message)
                .build();
    }

    public Long getId() {
        return id;
    }

    public User getTarget() {
        return target;
    }

    public User getSource() {
        return source;
    }

    public KoiType getKoiType() {
        return koiType;
    }

    public ReviewComment getCodeReviewComment() {
        return codeReviewComment;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "KoiHistory{" +
                "id=" + id +
                ", target=" + target.getId() +
                ", source=" + source.getId() +
                ", koiType=" + koiType +
                ", codeReviewComment=" + codeReviewComment.getId() +
                ", message='" + message + '\'' +
                '}';
    }
}
