package com.codekoi.apiserver.review.controller.response;

import com.codekoi.apiserver.review.dto.BasicCodeReview;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class HotCodeReviewListResponse {

    private List<BasicCodeReview> reviews = new ArrayList<>();

    public HotCodeReviewListResponse(List<BasicCodeReview> reviews) {
        this.reviews = reviews;
    }
}
