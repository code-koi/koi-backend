package com.codekoi.domain.comment;

import com.codekoi.domain.like.Like;
import com.codekoi.domain.review.CodeReview;
import com.codekoi.domain.user.User;
import com.codekoi.model.TimeBaseEntity;
import jakarta.persistence.*;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

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

    protected ReviewComment() {
    }

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    @Builder
    private ReviewComment(Long id, CodeReview codeReview, User user, String content, int likeCount, List<Like> likes) {
        this.id = id;
        this.codeReview = codeReview;
        this.user = user;
        this.content = content;
        this.likeCount = likeCount;
        this.likes = likes;
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

    public List<Like> getLikes() {
        return likes;
    }
}
