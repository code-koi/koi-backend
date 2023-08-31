package com.codekoi.apiserver.review.dto;

import com.codekoi.domain.review.CodeReviewStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class CodeReviewListCondition {

    private CodeReviewStatus status;

    @JsonProperty("skillId")
    private List<Long> skillIds;

    private String title;
    private Long lastId;

    public CodeReviewListCondition(CodeReviewStatus status, List<Long> skillIds, String title, Long lastId) {
        this.status = status;
        this.skillIds = Objects.requireNonNullElse(skillIds, new ArrayList<>());
        this.title = title;
        this.lastId = lastId;
    }
}
