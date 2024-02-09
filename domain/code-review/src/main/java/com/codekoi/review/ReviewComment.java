package com.codekoi.review;

import com.codekoi.common.TimeBaseEntity;
import com.codekoi.user.User;
import jakarta.persistence.*;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "review_comment")
public class ReviewComment extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_comment_id")
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
    private List<CommentLike> commentLikes = new ArrayList<>();

    protected ReviewComment() {
    }

    @Builder
    private ReviewComment(Long id, CodeReview codeReview, User user, String content, int likeCount, List<CommentLike> commentLikes) {
        this.id = id;
        this.codeReview = codeReview;
        this.user = user;
        this.content = content;
        this.likeCount = likeCount;
        this.commentLikes = Objects.requireNonNullElse(commentLikes, new ArrayList<>());
    }

    public static ReviewComment of(User user, CodeReview codeReview, String content) {
        return ReviewComment.builder()
                .user(user)
                .codeReview(codeReview)
                .content(content)
                .likeCount(0)
                .build();
    }

    public boolean isMyComment(Long userId) {
        return this.getUser().getId().equals(userId);
    }

    public void update(String content) {
        this.content = content;
    }

    public void addLikeOne() {
        this.likeCount += 1;
    }

    public void minusLikeOne() {
        this.likeCount -= 1;
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

    public String getContent() {
        return content;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public List<CommentLike> getCommentLikes() {
        return commentLikes;
    }

    @Override
    public String toString() {
        return "ReviewComment{" +
                "id=" + id +
                ", codeReview=" + codeReview.getId() +
                ", user=" + user.getId() +
                ", content='" + content + '\'' +
                ", likeCount=" + likeCount +
                '}';
    }
}
