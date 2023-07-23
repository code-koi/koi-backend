package com.codekoi.apiserver.like.controller;

import com.codekoi.coreweb.jwt.AuthInfo;
import com.codekoi.coreweb.jwt.AuthenticationPrincipal;
import com.codekoi.domain.like.usecase.LikeReviewCommentUseCase;
import com.codekoi.domain.like.usecase.UnlikeReviewCommentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeRestController {

    private final LikeReviewCommentUseCase likeReviewCommentUseCase;
    private final UnlikeReviewCommentUseCase unlikeReviewCommentUseCase;

    @PostMapping("/code-comments/{commentId}")
    public void like(@AuthenticationPrincipal AuthInfo authInfo, @PathVariable Long commentId) {
        likeReviewCommentUseCase.command(new LikeReviewCommentUseCase.Command(authInfo.getUserId(), commentId));
    }

    @DeleteMapping("/code-comments/{commentId}")
    public void unlike(@AuthenticationPrincipal AuthInfo authInfo, @PathVariable Long commentId) {
        unlikeReviewCommentUseCase.command(new UnlikeReviewCommentUseCase.Command(authInfo.getUserId(), commentId));
    }
}
