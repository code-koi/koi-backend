package com.codekoi.domain.review.repository;

public class CodeReviewNotFoundException extends RuntimeException {
    public CodeReviewNotFoundException(Long codeReviewId) {
        super("CodeReview: " + codeReviewId + "이 존재하지 않습니다.");
    }
}
