package com.codekoi.apiserver.review.controller.response;

import com.codekoi.apiserver.review.dto.UserCodeReviewDto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UserDetailCodeReviewListResponse {

    private List<UserCodeReviewDto> reviews = new ArrayList<>();

    public UserDetailCodeReviewListResponse(List<UserCodeReviewDto> reviews) {
        this.reviews = reviews;
    }

}
