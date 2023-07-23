package com.codekoi.domain.like.entity;

import com.codekoi.domain.comment.entity.ReviewComment;
import com.codekoi.domain.user.entity.User;
import com.codekoi.model.TimeBaseEntity;
import jakarta.persistence.*;
import lombok.Builder;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "likes")
public class Like extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likes_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "review_comment_id")
    private ReviewComment comment;

    protected Like() {
    }

    @Builder
    private Like(Long id, User user, ReviewComment comment) {
        this.id = id;
        this.user = user;
        this.comment = comment;
    }

    public static Like of(User user, ReviewComment comment) {
        final Like like = Like.builder()
                .user(user)
                .comment(comment)
                .build();

        comment.getLikes().add(like);

        return like;
    }

    public boolean isMyLike(Long userId) {
        return user.getId().equals(userId);
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public ReviewComment getComment() {
        return comment;
    }
}
