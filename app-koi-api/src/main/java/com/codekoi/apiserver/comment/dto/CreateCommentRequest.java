package com.codekoi.apiserver.comment.dto;


public record CreateCommentRequest(
        Long reviewId,
        String content
) {
}
