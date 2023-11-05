package com.codekoi.apiserver.review.controller.request;

import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record UpdateCodeReveiwRequest(
        @NotBlank String title,
        @NotBlank String content,
        List<Long> skillIds
) {
    public UpdateCodeReveiwRequest(String title, String content, List<Long> skillIds) {
        this.title = title;
        this.content = content;
        this.skillIds = Objects.requireNonNullElse(skillIds, new ArrayList<>());
    }
}
