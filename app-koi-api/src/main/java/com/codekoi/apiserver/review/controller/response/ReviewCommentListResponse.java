package com.codekoi.apiserver.review.controller.response;

import com.codekoi.apiserver.comment.dto.CommentReviewDetailDto;
import lombok.Getter;

import java.util.List;

@Getter
public class ReviewCommentListResponse {
    private List<CommentReviewDetailDto> comments;

    public ReviewCommentListResponse(List<CommentReviewDetailDto> comments) {
        this.comments = comments;
    }
}
