package com.codekoi.apiserver.review.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CodeReviewListCondition {

    private Boolean pending;
    private Boolean resolved;
    private List<String> tag = new ArrayList<>();
    private String keyword;
    private Long lastReviewId;

    public CodeReviewListCondition(Boolean pending, Boolean resolved, List<String> tag, Long lastReviewId, String keyword) {
        this.pending = pending;
        this.resolved = resolved;
        this.tag = tag;
        this.lastReviewId = lastReviewId;
        this.keyword = keyword;
    }
}
