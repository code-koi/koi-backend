package com.codekoi.apiserver.comment.dto;


public record CreateCommentRequest(
        Long reviewId,
        Long parentId,
        String content
) {
}
