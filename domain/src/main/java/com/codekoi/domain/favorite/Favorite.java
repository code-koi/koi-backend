package com.codekoi.domain.favorite;

import com.codekoi.domain.review.CodeReview;
import com.codekoi.domain.user.User;
import com.codekoi.model.TimeBaseEntity;
import jakarta.persistence.*;
import lombok.Builder;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "favorite")
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

    protected Favorite() {
    }

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

    public Long getId() {
        return id;
    }

    public CodeReview getCodeReview() {
        return codeReview;
    }

    public User getUser() {
        return user;
    }
}
