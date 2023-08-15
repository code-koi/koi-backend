package com.codekoi.apiserver.comment.controller;

import com.codekoi.apiserver.comment.controller.response.HotCommentsResponse;
import com.codekoi.apiserver.comment.dto.HotReviewComment;
import com.codekoi.apiserver.comment.service.ReviewCommentQueryService;
import com.codekoi.coreweb.jwt.AuthInfo;
import com.codekoi.coreweb.jwt.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentRestController {

    private final ReviewCommentQueryService reviewCommentQueryService;


    @GetMapping("/hot")
    public HotCommentsResponse getHotComments(@Principal AuthInfo sessionUser) {
        final List<HotReviewComment> hotComments = reviewCommentQueryService.getHotComments(sessionUser.getUserId());
        return new HotCommentsResponse(hotComments);
    }

}
