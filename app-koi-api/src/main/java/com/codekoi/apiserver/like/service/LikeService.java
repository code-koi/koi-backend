package com.codekoi.apiserver.like.service;

import com.codekoi.review.usecase.LikeReviewCommentUseCase;
import com.codekoi.review.usecase.UnlikeReviewCommentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {

    private final LikeReviewCommentUseCase likeReviewCommentUseCase;
    private final UnlikeReviewCommentUseCase unlikeReviewCommentUseCase;

    public void like(Long userId, Long commentId) {
        likeReviewCommentUseCase.command(new LikeReviewCommentUseCase.Command(userId, commentId));
    }

    public void unlike(Long userId, Long commentId) {
        unlikeReviewCommentUseCase.command(new UnlikeReviewCommentUseCase.Command(userId, commentId));
    }
}
