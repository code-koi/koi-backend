package com.codekoi.apiserver.comment.controller;

import com.codekoi.apiserver.comment.controller.response.HotCommentsResponse;
import com.codekoi.apiserver.comment.dto.CreateCommentRequest;
import com.codekoi.apiserver.comment.dto.HotReviewComment;
import com.codekoi.apiserver.comment.dto.UpdateCommentRequest;
import com.codekoi.apiserver.comment.service.ReviewCommentQueryService;
import com.codekoi.apiserver.comment.service.ReviewCommentService;
import com.codekoi.coreweb.jwt.AuthInfo;
import com.codekoi.coreweb.jwt.AuthenticationPrincipal;
import com.codekoi.coreweb.jwt.Principal;
import com.codekoi.response.SimpleIdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentRestController {

    private final ReviewCommentQueryService reviewCommentQueryService;
    private final ReviewCommentService reviewCommentService;

    @PostMapping
    public SimpleIdResponse create(@AuthenticationPrincipal AuthInfo sessionUser, @RequestBody CreateCommentRequest request) {
        final Long commentId = reviewCommentService.create(request.reviewId(), sessionUser.getUserId(), request.content());

        return new SimpleIdResponse(commentId);
    }

    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable Long commentId, @AuthenticationPrincipal AuthInfo sessionUser) {
        reviewCommentService.delete(commentId, sessionUser.getUserId());
    }

    @PostMapping("/{commentId}")
    public void update(@PathVariable Long commentId, @AuthenticationPrincipal AuthInfo sessionUser,
                       @RequestBody UpdateCommentRequest request) {
        reviewCommentService.update(commentId, sessionUser.getUserId(), request.content());
    }

    @GetMapping("/hot")
    public HotCommentsResponse getHotComments(@Principal AuthInfo sessionUser) {
        final List<HotReviewComment> hotComments = reviewCommentQueryService.getHotComments(sessionUser.getUserId());
        return new HotCommentsResponse(hotComments);
    }
}
