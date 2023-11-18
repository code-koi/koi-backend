package com.codekoi.apiserver.review.vo;

import com.codekoi.domain.comment.ReviewComment;
import com.codekoi.domain.favorite.Favorite;
import com.codekoi.domain.like.Like;
import com.codekoi.domain.review.CodeReview;

import java.time.LocalDateTime;

public record Activity(Activity.Type type, Long id, String targetText, LocalDateTime createdAt) {

    public static Activity from(CodeReview codeReview) {
        return new Activity(Type.REVIEW, codeReview.getId(), codeReview.getTitle(), codeReview.getCreatedAt());
    }

    public static Activity from(ReviewComment reviewComment) {
        Long codeReviewId = reviewComment.getCodeReview().getId();
        return new Activity(Type.COMMENT, codeReviewId, reviewComment.getContent(), reviewComment.getCreatedAt());
    }

    public static Activity from(Like like) {
        Long codeReviewId = like.getComment().getCodeReview().getId();
        String content = like.getComment().getContent();
        return new Activity(Type.LIKE, codeReviewId, content, like.getCreatedAt());
    }

    public static Activity from(Favorite favorite) {
        Long codeReviewId = favorite.getCodeReview().getId();
        String content = favorite.getCodeReview().getContent();
        return new Activity(Type.FAVORITE, codeReviewId, content, favorite.getCreatedAt());
    }

    public enum Type {
        REVIEW("리뷰 작성"), COMMENT("리뷰 요청"), LIKE("좋아요"), FAVORITE("즐겨찾기");

        public final String description;

        Type(String description) {
            this.description = description;
        }
    }
}