package com.codekoi.apiserver.review.controller.response;

import com.codekoi.apiserver.comment.dto.UserCodeCommentDto;
import lombok.Getter;

import java.util.List;

@Getter
public class UserCommentedReviewListResponse {

    private List<UserCodeCommentDto> comments;

    public UserCommentedReviewListResponse(List<UserCodeCommentDto> comments) {
        this.comments = comments;
    }

    public static UserCommentedReviewListResponse from(List<UserCodeCommentDto> comments) {
        return new UserCommentedReviewListResponse(comments);
    }
}
