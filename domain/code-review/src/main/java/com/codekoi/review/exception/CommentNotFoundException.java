package com.codekoi.review.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(Long commentId) {
        super("Comment: " + commentId + "를 찾을 수 없습니다.");
    }
}
