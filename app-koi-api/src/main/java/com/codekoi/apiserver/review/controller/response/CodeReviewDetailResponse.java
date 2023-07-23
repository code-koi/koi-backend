package com.codekoi.apiserver.review.controller.response;

import com.codekoi.apiserver.review.dto.CodeReviewDetailDto;
import lombok.Getter;

@Getter
public class CodeReviewDetailResponse {
    private CodeReviewDetailDto review;

    public CodeReviewDetailResponse(CodeReviewDetailDto review) {
        this.review = review;
    }
}
