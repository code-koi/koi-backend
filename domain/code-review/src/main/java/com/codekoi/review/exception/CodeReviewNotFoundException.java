package com.codekoi.review.exception;

public class CodeReviewNotFoundException extends RuntimeException {
    public CodeReviewNotFoundException(Long codeReviewId) {
        super("CodeReview: " + codeReviewId + "이 존재하지 않습니다.");
    }
}
