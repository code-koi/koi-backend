package com.codekoi.apiserver.review.dto;

import com.codekoi.domain.review.CodeReviewStatus;
import lombok.Getter;

import java.util.List;

@Getter
public class CodeReviewListCondition {

    private CodeReviewStatus status;
    private List<String> tag;
    private String title;
    private Long lastId;

    public CodeReviewListCondition(CodeReviewStatus status, List<String> tag, String title, Long lastId) {
        this.status = status;
        this.tag = tag;
        this.title = title;
        this.lastId = lastId;
    }
}
