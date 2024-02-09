package com.codekoi.review;

import com.codekoi.common.TimeBaseEntity;
import com.codekoi.user.User;
import jakarta.persistence.*;
import lombok.Builder;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "likes")
public class CommentLike extends TimeBaseEntity {

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

    protected CommentLike() {
    }

    @Builder
    private CommentLike(Long id, User user, ReviewComment comment) {
        this.id = id;
        this.user = user;
        this.comment = comment;
    }

    public static CommentLike of(User user, ReviewComment comment) {
        final CommentLike commentLike = CommentLike.builder()
                .user(user)
                .comment(comment)
                .build();

        comment.getCommentLikes().add(commentLike);

        return commentLike;
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
