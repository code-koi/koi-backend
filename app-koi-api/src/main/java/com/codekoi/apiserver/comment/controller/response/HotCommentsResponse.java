package com.codekoi.apiserver.comment.controller.response;

import com.codekoi.apiserver.comment.dto.HotReviewComment;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class HotCommentsResponse {
    List<HotReviewComment> comments = new ArrayList<>();

    public HotCommentsResponse(List<HotReviewComment> comments) {
        this.comments = comments;
    }
}
