package com.codekoi.apiserver.review.controller.response;

import com.codekoi.apiserver.review.dto.HotCodeReview;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class HotCodeReviewListResponse {

    private List<HotCodeReview> reviews = new ArrayList<>();

    public HotCodeReviewListResponse(List<HotCodeReview> reviews) {
        this.reviews = reviews;
    }
}
